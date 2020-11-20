package com.sedi.routelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.sedi.routelist.R
import com.sedi.routelist.commons.log
import com.sedi.routelist.models.Address
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
        MapsInitializer.setApiKey(resources.getString(R.string.api_key))
        mapView.onCreate(mapViewBundle)
        //get map instance
        mapView.getMapAsync(this)
    }


    override fun onMapReady(map: HuaweiMap) {
        //get map instance in a callback method
        log("onMapReady")
        hMap = map
        hMap!!.isMyLocationEnabled = true
        hMap!!.uiSettings.isMyLocationButtonEnabled = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
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