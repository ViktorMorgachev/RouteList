package com.sedi.routelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleObserver
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sedi.routelist.R
import com.sedi.routelist.ui.fragment.NoticeFragment

class MainActivity : AppCompatActivity(), LifecycleObserver {

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
            addFragment(NoticeFragment())
            addFragment(NoticeFragment())
            addFragment(NoticeFragment())
        }
        viewPager.adapter = viewPagerAdapter
    }

}
