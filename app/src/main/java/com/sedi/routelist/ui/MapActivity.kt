package com.sedi.routelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.sedi.routelist.R
import com.sedi.routelist.commons.ExtraNames
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.models.Address
import com.sedi.routelist.models.Notice
import com.sedi.routelist.presenters.IClickListener
import com.sedi.routelist.ui.fragment.NoticeFragment
import kotlinx.android.synthetic.main.huawei_map_layout.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var hMap: HuaweiMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.huawei_map_layout)
        var mapViewBundle: Bundle? = null

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        // please replace "Your API key" with api_key field value in
        // agconnect-services.json
        MapsInitializer.setApiKey("CgB6e3x9u6Xmi/Y6ykebG2lYedCpeyc1sOgxO0kSOqjiuwYLiJAuxB/XqKOsuCSL7hngyxBGWWGw1rafWzIEmUaU")
        mapView.onCreate(mapViewBundle)
        //get map instance
        mapView.getMapAsync(this)
    }


    override fun onMapReady(map: HuaweiMap) {
        //get map instance in a callback method
        log(LOG_LEVEL.INFO, "onMapReady")
        hMap = map
        hMap!!.isMyLocationEnabled = true
        hMap!!.uiSettings.isMyLocationButtonEnabled = true
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    companion object {
        private var addresFrom: Address? = null
        private var addresTo: Address? = null
        private val instance = MapActivity()
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        fun init(
            addressFirst: Address?,
            addressSecond: Address?
        ) {
            addresFrom = addressFirst
            addresTo = addressSecond
        }
    }
}