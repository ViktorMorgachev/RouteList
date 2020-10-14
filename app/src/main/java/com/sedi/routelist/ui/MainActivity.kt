package com.sedi.routelist.ui

import android.app.Activity
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
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.commons.showToast
import com.sedi.routelist.models.*
import com.sedi.routelist.presenters.IClickListener
import com.sedi.routelist.presenters.IResultCalback
import com.sedi.routelist.ui.dialogfragment.ChooseLanguageDialog
import com.sedi.routelist.ui.fragment.NoticeFragment
import ru.sedi.customerclient.adapters.LanguageAdapter
import java.util.*


class MainActivity : AppCompatActivity(), LifecycleObserver, IClickListener, IResultCalback {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: NoticesPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.notice_view_pager)

        runOnUiThread {
            MyApplication.instance.initDB(this)
        }

        // Если первый запуск
        if (PrefsManager.getIntance(applicationContext)
                .getValue(PrefsManager.PrefsKey.FIRST_START, true) as Boolean
        ) {
            viewPager.visibility = View.GONE
            val items = arrayListOf(
                Language(R.drawable.ic_china, R.string.zh, "zh"),
                Language(R.drawable.ic_germany, R.string.de, "de"),
                Language(R.drawable.ic_russia, R.string.ru, "ru"),
                Language(R.drawable.ic_en, R.string.en, "en"),
                Language(R.drawable.ic_kyrgyzstan, R.string.ky, "ky")
            )
            ChooseLanguageDialog(items, object : LanguageAdapter.ClickCallback {
                override fun onClicked(language: String) {
                    PrefsManager.getIntance(this@MainActivity)
                        .setValue(PrefsManager.PrefsKey.LOCALE, language)
                    updateLocale()
                    viewPager.visibility = View.VISIBLE
                }

            }).show(supportFragmentManager, "MyCustomFragment")
            PrefsManager.getIntance(applicationContext)
                .setValue(PrefsManager.PrefsKey.FIRST_START, true)
        } else {
            initNotices()
        }




        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)


    }

    private fun updateLocale() {
        try {
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            val localeCode: String =
                PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.LOCALE, "ru") as String
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

    fun addNotice(item: MenuItem) {
        pagerAdapter.addFragment(
            NoticeFragment.instance(
                Notice(),
                this,
                pagerAdapter,
                pagerAdapter.count
            )
        )
    }

    fun deleteNotice(item: MenuItem) {

        //Временный костыль
        if (pagerAdapter.count == 1) {
            showToast(
                this,
                MyApplication.instance.resources.getString(R.string.minimum_must_have_list)
            )
            return
        }
        // Remove current list and delete current Notice from DB
        pagerAdapter.removeFragment(pagerAdapter.getItem(pagerAdapter.noticeFragmentHelper.currentPosition - 1))
        asynkDeleteNotice(
            this,
            MyApplication.instance.getDB(),
            convertNoticeItemToRoomModel(pagerAdapter.noticeFragmentHelper.currentNotice)
        )
    }

    fun copyNotice(item: MenuItem) {
        pagerAdapter.noticeFragmentHelper.noticeForCopy =
            pagerAdapter.noticeFragmentHelper.currentNotice
    }

    fun pasteNotice(item: MenuItem) {
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


    interface PastNoticeCallback {
        fun pastNotice(notice: Notice)
    }

}
