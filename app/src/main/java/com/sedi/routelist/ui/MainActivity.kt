package com.sedi.routelist.ui

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.support.log.LogLevel
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.backgrounds.ConnectivityInformation
import com.sedi.routelist.backgrounds.ConnectivityListener
import com.sedi.routelist.backgrounds.OnConnectivityInformationChangedListener
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.commons.showToast
import com.sedi.routelist.models.*
import com.sedi.routelist.presenters.IAction
import com.sedi.routelist.presenters.IClickListener
import com.sedi.routelist.presenters.IResultCalback
import com.sedi.routelist.ui.dialogfragment.ChooseLanguageDialog
import com.sedi.routelist.ui.fragment.NoticeFragment
import java.util.*


class MainActivity : AppCompatActivity(), LifecycleObserver, IClickListener, IResultCalback,
    OnConnectivityInformationChangedListener {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: NoticesPagerAdapter
    private lateinit var connectivityListener: ConnectivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.notice_view_pager)

        runOnUiThread {
            MyApplication.instance.initDB(this)
        }


        connectivityListener = ConnectivityListener(this)
        connectivityListener.register()
        connectivityListener.setListener(this)


        // Если первый запуск
        if (PrefsManager.getIntance(applicationContext)
                .getValue(PrefsManager.PrefsKey.FIRST_START, true)
        ) {
            showLanguageChooseDialog(object : IAction {
                override fun action() {
                    updateLocale()
                    initNotices()
                    viewPager.visibility = View.VISIBLE
                    refreshActivity()
                }

            })
            viewPager.visibility = View.GONE
            PrefsManager.getIntance(applicationContext)
                .setValue(PrefsManager.PrefsKey.FIRST_START, false)
        } else {
            initNotices()
        }

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)

    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityListener.unregister()
    }

    fun showLanguageChooseDialog(action: IAction) {
        ChooseLanguageDialog(action).show(supportFragmentManager, "MyCustomFragment")
    }

    private fun updateLocale() {
        MyApplication.instance.updateLocale()
        try {
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            val localeCode =
                PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.LOCALE, "ru")
            log(LOG_LEVEL.INFO, "LocaleCode: $localeCode")
            conf.locale = Locale(localeCode)
            res.updateConfiguration(conf, dm)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    private fun initNotices() {
        asynkGetAllNotices(this, this, MyApplication.instance.getDB())
    }

    private fun setupViewPager(notices: List<Notice>) {
        pagerAdapter = NoticesPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        if (notices.isEmpty()) {
            pagerAdapter.addFragment(
                NoticeFragment.instance(
                    Notice(dbKey = 1),
                    this,
                    pagerAdapter,
                    1
                )
            )
        } else
            notices.forEachIndexed { index, notice ->
                pagerAdapter.addFragment(
                    NoticeFragment.instance(
                        notice,
                        this,
                        pagerAdapter,
                        index + 1
                    )
                )
            }
        viewPager.adapter = pagerAdapter
    }

    override fun onSave(notice: Notice, position: Int) {
        log(LOG_LEVEL.INFO, "SaveNotice: $position")
        asynkInsertNotice(
            convertNoticeItemToRoomModel(notice),
            this,
            MyApplication.instance.getDB(),
            position
        );
    }


    override fun onError(exception: Exception) {
        showToast(
            this,
            "Ошибка: ${exception.message}"
        )
    }

    override fun onSucces(answer: String, notices: List<Notice>) {
        if (answer.isNotEmpty()) {
            showToast(this, answer)
        } else setupViewPager(notices)
    }

    fun addNotice(item: MenuItem?) {
        pagerAdapter.addFragment(
            NoticeFragment.instance(
                Notice(),
                this,
                pagerAdapter,
                pagerAdapter.count + 1
            )
        )
    }

    fun showMapByAddresses(adressFrom: LatLng, adressTo: LatLng) {
        // Тут показать на чём будете передвигаться
        //  Велосипед, Машина, Пешком
    }

    fun deleteNotice(item: MenuItem?) {

        //Временный костыль
        if (pagerAdapter.count == 1) {
            showToast(
                this,
                MyApplication.instance.resources.getString(R.string.minimum_must_have_list)
            )
            return
        }
        // Remove current list and delete current Notice from DB
        pagerAdapter.noticeFragmentHelper.noticeForCopy = null
        pagerAdapter.removeFragment(pagerAdapter.getItem(pagerAdapter.noticeFragmentHelper.currentPosition - 1))
        asynkDeleteNotice(
            this,
            MyApplication.instance.getDB(),
            convertNoticeItemToRoomModel(pagerAdapter.noticeFragmentHelper.currentNotice)
        )
    }

    fun copyNotice(item: MenuItem?) {
        pagerAdapter.noticeFragmentHelper.noticeForCopy =
            pagerAdapter.noticeFragmentHelper.currentNotice
    }

    fun pasteNotice(item: MenuItem?) {
        if (pagerAdapter.noticeFragmentHelper.noticeForCopy == null) {
            showToast(
                this,
                MyApplication.instance.resources.getString(R.string.copy_before_paste),
                Toast.LENGTH_SHORT
            )
            return
        }
        try {
            (pagerAdapter.getItem(pagerAdapter.noticeFragmentHelper.currentPosition - 1) as PastNoticeCallback).pastNotice(
                pagerAdapter.noticeFragmentHelper.noticeForCopy!!
            )
        } catch (e: ClassCastException) {
            onError(e)
        } catch (e: Exception) {
            onError(e)
        }

    }

    fun change_locale(item: MenuItem) {
        showLanguageChooseDialog(object : IAction {
            override fun action() {
                updateLocale()
                refreshActivity()
            }
        })
    }

    private fun updateUI(hasNetwork: Boolean) {
    }


    interface PastNoticeCallback {
        fun pastNotice(notice: Notice)
    }

    fun refreshActivity() {
        val refresh = Intent(
            this, MainActivity::class.java
        )
        finish()
        startActivity(refresh)
    }

    override fun onConnectivityInformationChanged(connectivityInformation: ConnectivityInformation) {
        log(
            LOG_LEVEL.INFO,
            "ConnectivityInformation ${connectivityInformation.name} IsConnected: ${connectivityInformation.isConnected}"
        )
        runOnUiThread {
            when (connectivityInformation) {
                ConnectivityInformation.CONNECTED -> updateUI(true)
                ConnectivityInformation.WIFI -> updateUI(true)
                ConnectivityInformation.OTHER -> updateUI(true)
                ConnectivityInformation.DISCONNECTED -> updateUI(false)
                else -> updateUI(false)
            }
        }

    }


}
