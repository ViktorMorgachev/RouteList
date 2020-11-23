package com.sedi.routelist.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.site.widget.SearchFilter
import com.huawei.hms.site.widget.SearchIntent
import com.sedi.routelist.R
import com.sedi.routelist.commons.DoubleClickListener
import com.sedi.routelist.commons.gone
import com.sedi.routelist.commons.log
import com.sedi.routelist.commons.visible
import com.sedi.routelist.models.Address
import com.sedi.routelist.models.PrefsManager
import kotlinx.android.synthetic.main.huawei_map_layout.*
import kotlinx.android.synthetic.main.item_center_map.*


class MapActivity : BaseActivity(), OnMapReadyCallback, HuaweiMap.OnCameraIdleListener,
    HuaweiMap.OnMapClickListener {

    private var hMap: HuaweiMap? = null
    private var mapMode: Mode = Mode.GET_ROUTE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.huawei_map_layout)
        var mapViewBundle: Bundle? = null


        if (intent.extras?.get(
                KEY_WORK_MODE
            ) == Mode.GET_POINT.name
        ) {
            panel_map_center.visible()
            mapMode = Mode.GET_POINT

            iv_map_center.setOnClickListener {
                if (!tv_address.isVisible) {
                    tv_address.visible(500)
                } else {
                    tv_address.gone(500)
                }
            }

            tv_address.setOnClickListener {
                startActivityForResult(
                    searchIntent?.getIntent(this),
                    SearchIntent.SEARCH_REQUEST_CODE
                )
            }

        } else {
            mapMode = Mode.GET_ROUTE
            panel_map_center.gone()
        }

        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)

        if (searchIntent == null)
            searchIntent = SearchIntent().apply {
                setApiKey(resources.getString(R.string.api_key))
                setSearchFilter(SearchFilter().apply {
                    language = PrefsManager.getIntance(this@MapActivity)
                        .getValue(PrefsManager.PrefsKey.LOCALE, "en")
                })
            }


        MapsInitializer.setApiKey(resources.getString(R.string.api_key))
        mapView.onCreate(mapViewBundle)

        mapView.getMapAsync(this)
    }


    override fun onMapReady(map: HuaweiMap) {
        //get map instance in a callback method

        hMap = map
        hMap!!.isMyLocationEnabled = true
        hMap!!.uiSettings.isMyLocationButtonEnabled = true
        hMap!!.setLanguage(PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.LOCALE, ""))
        hMap!!.setOnCameraIdleListener(this)
        hMap!!.setOnMapClickListener(this)
        log("onMapReady")
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

    override fun onCameraIdle() {
        if (tv_address != null)
            tv_address.text = "${hMap?.cameraPosition?.target}"
    }


    override fun onMapClick(location: LatLng?) {
        if (location != null) {
            hMap?.animateCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            if (SearchIntent.isSuccess(resultCode)) {
                if (searchIntent != null) {
                    val site: Site = searchIntent!!.getSiteFromIntent(data)
                    hMap?.animateCamera(
                        CameraUpdateFactory.newLatLng(LatLng(site.location.lat, site.location.lng))
                    )
                    tv_address.text = site.formatAddress
                }
            }
        }
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
        addresFrom = null
        addresTo = null
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
        const val KEY_WORK_MODE = "KEY_WORK_MODE"
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

enum class Mode {
    GET_ROUTE,
    GET_POINT
}