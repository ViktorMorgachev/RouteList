package com.sedi.routelist.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.huawei.hms.site.widget.SearchFragment
import com.sedi.routelist.R


class SearchAddressActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search_address)

        val searchFragment =  (supportFragmentManager.findFragmentById(R.id.widget_fragment) as SearchFragment)
        searchFragment.setApiKey(resources.getString(R.string.api_key))
    }
}