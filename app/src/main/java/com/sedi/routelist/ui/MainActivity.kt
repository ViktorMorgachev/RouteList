package com.sedi.routelist.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.backgrounds.ConnectivityInformation
import com.sedi.routelist.backgrounds.ConnectivityListener
import com.sedi.routelist.backgrounds.OnConnectivityInformationChangedListener
import com.sedi.routelist.commons.*
import com.sedi.routelist.models.*
import com.sedi.routelist.presenters.IAction
import com.sedi.routelist.presenters.IClickListener
import com.sedi.routelist.presenters.IResultCalback
import com.sedi.routelist.ui.dialogfragment.ChooseLanguageDialog
import com.sedi.routelist.ui.fragment.FragmentListenerCallback
import com.sedi.routelist.ui.fragment.NoticeFragment
import java.util.*


class MainActivity : AppCompatActivity(), LifecycleObserver, IClickListener, IResultCalback,
    OnConnectivityInformationChangedListener, FragmentListenerCallback {

    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var pagerAdapter: NoticesPagerAdapter? = null
    private var connectivityListener: ConnectivityListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.notice_view_pager)

        runOnUiThread {
            MyApplication.instance.initDB(this)
        }


        connectivityListener = ConnectivityListener(this)
        connectivityListener?.register()
        connectivityListener?.setListener(this)


        // Если первый запуск
        if (PrefsManager.getIntance(applicationContext)
                .getValue(PrefsManager.PrefsKey.FIRST_START, true)
        ) {
            showLanguageChooseDialog(object : IAction {
                override fun action() {
                    updateLocale()
                    initNotices()
                    viewPager?.visibility = View.VISIBLE
                    refreshActivity()
                }

            })
            viewPager?.visibility = View.GONE
            PrefsManager.getIntance(applicationContext)
                .setValue(PrefsManager.PrefsKey.FIRST_START, false)
        } else {
            initNotices()
        }

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout?.setupWithViewPager(viewPager, true)

    }


    override fun onDestroy() {
        super.onDestroy()
        connectivityListener?.unregister()
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
            log("LocaleCode: $localeCode")
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
            pagerAdapter?.addFragment(
                NoticeFragment.instance(
                    Notice(dbKey = 1),
                    this,
                    pagerAdapter!!,
                    1
                )
            )
        } else
            notices.forEachIndexed { index, notice ->
                pagerAdapter?.addFragment(
                    NoticeFragment.instance(
                        notice,
                        this,
                        pagerAdapter!!,
                        index + 1
                    )
                )
            }
        viewPager?.adapter = pagerAdapter
    }

    override fun onSave(notice: Notice, position: Int) {
        log("SaveNotice: $position")
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
        pagerAdapter?.addFragment(
            NoticeFragment.instance(
                Notice(),
                this,
                pagerAdapter!!,
                pagerAdapter!!.count + 1
            )
        )
    }


    fun deleteNotice(item: MenuItem?) {

        if (pagerAdapter == null) return
        //Временный костыль
        if (pagerAdapter?.count == 1) {
            showToast(
                this,
                MyApplication.instance.resources.getString(R.string.minimum_must_have_list)
            )
            return
        }
        // Remove current list and delete current Notice from DB
        pagerAdapter?.noticeFragmentHelper?.noticeForCopy = null
        pagerAdapter?.removeFragment(pagerAdapter!!.getItem(pagerAdapter!!.noticeFragmentHelper.currentPosition - 1))
        asynkDeleteNotice(
            this,
            MyApplication.instance.getDB(),
            convertNoticeItemToRoomModel(pagerAdapter!!.noticeFragmentHelper.currentNotice)
        )
    }

    fun copyNotice(item: MenuItem?) {
        pagerAdapter?.noticeFragmentHelper?.noticeForCopy =
            pagerAdapter?.noticeFragmentHelper?.currentNotice
    }

    fun pasteNotice(item: MenuItem?) {

        if (pagerAdapter == null) return

        if (pagerAdapter?.noticeFragmentHelper?.noticeForCopy == null) {
            showToast(
                this,
                MyApplication.instance.resources.getString(R.string.copy_before_paste),
                Toast.LENGTH_SHORT
            )
            return
        }
        try {
            (pagerAdapter?.getItem(pagerAdapter!!.noticeFragmentHelper.currentPosition - 1) as PastNoticeCallback)
                .pastNotice(
                    pagerAdapter!!.noticeFragmentHelper.noticeForCopy!!
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
        pagerAdapter?.updateFragments(hasNetwork)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface UpdateUIListener {
        fun updateUI(hasNetwork: Boolean)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showMapActivity(addressFrom: Address?, addressTo: Address?) {
        MapActivity.init(addressFrom, addressTo)
        intent.putExtra(MapActivity.KEY_WORK_MODE, Mode.GET_ROUTE)
        startActivity(Intent(this, MapActivity::class.java))
    }

    override fun addessFromMap() {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra(MapActivity.KEY_WORK_MODE, Mode.GET_POINT)
        startActivity(Intent(this, MapActivity::class.java))
    }

}
