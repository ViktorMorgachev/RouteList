package com.sedi.routelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.site.api.model.Coordinate
import com.huawei.hms.site.api.model.LocationType
import com.huawei.hms.site.widget.SearchFilter
import com.huawei.hms.site.widget.SearchIntent
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.backgrounds.LocationManager
import com.sedi.routelist.models.PrefsManager

open class BaseActivity : AppCompatActivity() {
    protected var searchIntent: SearchIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (searchIntent == null)
            searchIntent = SearchIntent().apply {
                setApiKey(resources.getString(R.string.api_key))
                setSearchFilter(SearchFilter().apply {
                    language = MyApplication.language
                    val lastLocation = LocationManager.getLastLocation()
                    location = Coordinate(lastLocation.latitude, lastLocation.longitude)
                    radius = 30_000
                })
            }
    }
}