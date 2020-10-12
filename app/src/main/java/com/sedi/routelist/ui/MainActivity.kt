package com.sedi.routelist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.commons.asynkExecute
import com.sedi.routelist.commons.showToast
import com.sedi.routelist.models.*
import com.sedi.routelist.presenters.IClickListener
import com.sedi.routelist.presenters.IResultCalback
import com.sedi.routelist.ui.fragment.NoticeFragment


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

        initNotices()


        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    private fun initNotices() {
        asynkGetAllNotices(this, this,  MyApplication.instance.getDB())
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
            showToast(this, MyApplication.instance.resources.getString(R.string.minimum_must_have_list))
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
            showToast(this, MyApplication.instance.resources.getString(R.string.copy_before_paste), Toast.LENGTH_SHORT)
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
