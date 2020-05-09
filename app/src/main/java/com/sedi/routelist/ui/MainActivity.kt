package com.sedi.routelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sedi.routelist.R
import com.sedi.routelist.models.Notice
import com.sedi.routelist.presenters.ISaveListener
import com.sedi.routelist.ui.fragment.NoticeFragment

class MainActivity : AppCompatActivity(), LifecycleObserver, ISaveListener {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerAdapter: NoticesPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.notice_view_pager)
        setupViewPager(viewPager)

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)


    }

    private fun setupViewPager(viewPager: ViewPager) {
        viewPagerAdapter = NoticesPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ).apply {
            addFragment(NoticeFragment.newInstance(Notice(fio = "Настя"), this@MainActivity))
            addFragment(NoticeFragment.newInstance(Notice(fio = "Виктор"), this@MainActivity))
        }
        viewPager.adapter = viewPagerAdapter
    }

    override fun onSave(notice: Notice) {
        // Nothing
    }

}
