package com.sedi.routelist.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sedi.routelist.models.Notice

class NoticesPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    val noticeFragmentHelper = NoticeFragmentHelper()
    private val mFragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)

        notifyDataSetChanged()

    }

    fun removeFragment(fragment: Fragment) {
        mFragmentList.remove(fragment)
        notifyDataSetChanged()
    }

    class NoticeFragmentHelper() {
        var currentPosition: Int = 0;
        var currentNotice: Notice = Notice()
        var noticeForCopy: Notice? = null
    }

}