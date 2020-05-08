package com.sedi.routelist.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sedi.routelist.R
import com.sedi.routelist.models.Notice
import com.sedi.routelist.models.NoticeRoomModel
import com.sedi.routelist.models.asynkSaveNotice
import com.sedi.routelist.models.convertNoticeItemToRoomModel
import com.sedi.routelist.presenters.IResultCalback
import com.sedi.routelist.presenters.ISaveListener
import com.sedi.routelist.showToast

class MainActivity : AppCompatActivity(), ISaveListener, IResultCalback {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.notice_view_pager)
        pagerAdapter = NoticeAdapter(
            this,
            notices = listOf(
                Notice(""),
                Notice("")
            ),
            saveListener = this
        )
        viewPager.adapter = pagerAdapter

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)

        showToast(this, "PagerAdapter size: ${pagerAdapter.count}")

    }

    override fun onSave(notice: Notice) {
        asynkSaveNotice(convertNoticeItemToRoomModel(notice), this)
    }

    override fun onError(exception: Exception) {
        TODO("Not yet implemented")
    }

    override fun onSingleComplete(data: NoticeRoomModel?) {
        TODO("Not yet implemented")
    }

    override fun onSucces() {
        Toast.makeText(this, "Успешно сохраненно", Toast.LENGTH_SHORT).show()
    }
}
