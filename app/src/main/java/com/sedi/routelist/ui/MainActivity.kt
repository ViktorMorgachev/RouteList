package com.sedi.routelist.ui

import android.os.Bundle
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
import com.sedi.routelist.ui.fragment.NoticeFragment

class MainActivity : AppCompatActivity(), LifecycleObserver, IClickListener, IResultCalback {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerAdapter: NoticesPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.notice_view_pager)

        initNotices()

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)

    }

    private fun initNotices() {
        asynkGetAllNotices(this, MyApplication.instance.getDB(this))
    }

    private fun setupViewPager(notices: List<Notice>) {
        viewPagerAdapter = NoticesPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        if (notices.isEmpty()) {
            viewPagerAdapter.addFragment(NoticeFragment.instance(Notice(), this, 0))
        } else
            notices.forEachIndexed { index, notice ->
                viewPagerAdapter.addFragment(NoticeFragment.instance(notice, this, index))
            }
        viewPager.adapter = viewPagerAdapter
    }

    override fun onSave(notice: Notice, position: Int) {
        asynkInsertNotice(
            convertNoticeItemToRoomModel(notice),
            this,
            MyApplication.instance.getDB(this),
            position
        );
    }

    override fun onDelete(notice: Notice, position: Int) {
        asynkDeleteNotice(this, MyApplication.instance.getDB(this))
    }

    override fun onError(exception: Exception) {
        showToast(
            this,
            "Ошибка: ${exception.message}"
        )
    }

    override fun onSingleComplete(data: NoticeRoomModel?) {
        TODO("Not yet implemented")
    }

    override fun onSucces(answer: String, notices: List<Notice>) {
        if (!answer.isEmpty()) {
            showToast(this, answer)
        } else setupViewPager(notices)
    }


}
