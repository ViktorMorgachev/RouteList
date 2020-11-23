package com.sedi.routelist.ui

import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.site.widget.SearchFilter
import com.huawei.hms.site.widget.SearchIntent
import com.sedi.routelist.R
import com.sedi.routelist.models.PrefsManager

open class BaseActivity : AppCompatActivity() {
    protected var searchIntent : SearchIntent? = null
}