package com.sedi.routelist.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.core.view.isVisible
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.site.widget.SearchFilter
import com.huawei.hms.site.widget.SearchIntent
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.commons.gone
import com.sedi.routelist.commons.log
import com.sedi.routelist.commons.visible
import com.sedi.routelist.models.Address
import com.sedi.routelist.network.GeoCodingType
import com.sedi.routelist.network.GeocodingService
import com.sedi.routelist.network.geocode.reverse.ReverseGeocode
import com.sedi.routelist.presenters.IActionResult
import kotlinx.android.synthetic.main.huawei_map_layout.*
import kotlinx.android.synthetic.main.item_center_map.*


class MapActivity : BaseActivity(), OnMapReadyCallback, HuaweiMap.OnCameraIdleListener,
    HuaweiMap.OnMapClickListener {

    private var hMap: HuaweiMap? = null
    private var mapMode: Mode = Mode.GET_ROUTE
    private var currentAddress: Address? = null

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

            currentAddress = RememberData.remindMe(RememberData.KEYS.ADDRESS.value) as Address

            iv_map_center.setOnClickListener {
                if (!ll_address_info.isVisible) {
                    ll_address_info.visible(500)
                } else {
                    ll_address_info.gone(500)
                }
            }

            tv_address.setOnClickListener {
                startActivityForResult(
                    searchIntent?.getIntent(this),
                    SearchIntent.SEARCH_REQUEST_CODE
                )
            }

            btn_save_address.setOnClickListener {
                RememberData.rememberMe(RememberData.KEYS.ADDRESS.value, currentAddress!!)
                setResult(RESULT_OK, intent);
                finish();
            }

        } else {
            mapMode = Mode.GET_ROUTE
            panel_map_center.gone()
        }

        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)

        MapsInitializer.setApiKey(resources.getString(R.string.api_key))
        mapView.onCreate(mapViewBundle)

        mapView.getMapAsync(this)
    }


    override fun onMapReady(map: HuaweiMap) {
        //get map instance in a callback method

        if (mapMode == Mode.GET_POINT)
            if (currentAddress != null) {

                log("CurrentAddress: $currentAddress")
                tv_address.setText(currentAddress!!.address)
                // Отключаем слушатель на время
                removeMapIdleListenerForTimeMilliss(600)
                onMapClick(currentAddress!!.location)
            }

        hMap = map
        hMap!!.isMyLocationEnabled = true
        hMap!!.uiSettings.isMyLocationButtonEnabled = true
        hMap!!.setLanguage(MyApplication.language)
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
        val location = hMap?.cameraPosition?.target

        if (location != null) {
            GeocodingService.reverseGeocoding(
                geoCodingType,
                location,
                object : IActionResult {
                    override fun result(result: Any?, exception: Exception?) {
                        if (result != null) {
                            if (geoCodingType == GeoCodingType.HUAWEI) {
                                val data = result as ReverseGeocode
                                if (data.sites != null && data.sites.isNotEmpty()) {
                                    tv_address.post {
                                        tv_address.text = data.sites[0].formatAddress
                                    }
                                    currentAddress = Address(
                                        data.sites[0].formatAddress,
                                        LatLng(
                                            data.sites[0].location.lat,
                                            data.sites[0].location.lng
                                        )
                                    )
                                }
                            } else {
                                val data = result as Address
                                currentAddress = data
                                tv_address.post {
                                    tv_address.text = data.address
                                }
                            }

                        } else if (exception != null) {
                            log(exception)
                        }
                    }
                })
        }
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
                    currentAddress =
                        Address(site.formatAddress, LatLng(site.location.lat, site.location.lng))
                    log("currentAddress: $currentAddress")
                    removeMapListener()
                    hMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                site.location.lat,
                                site.location.lng
                            ), 12f
                        ), object : HuaweiMap.CancelableCallback {
                            override fun onFinish() {
                                hMap?.setOnCameraIdleListener(this@MapActivity)
                            }

                            override fun onCancel() {
                                hMap?.setOnCameraIdleListener(this@MapActivity)
                            }
                        })
                }
            }
        }
    }

    private fun removeMapListener() {
        hMap?.setOnCameraIdleListener { }
    }

    private fun removeMapIdleListenerForTimeMilliss(duration: Long = 600) {
        hMap?.setOnCameraIdleListener { }
        AsyncTask.execute {
            Thread.sleep(duration)
            runOnUiThread {
                hMap?.setOnCameraIdleListener(this)
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
        const val KEY_REQUEST_CODE_GET_POINT = 2
        const val KEY_WORK_MODE = "KEY_WORK_MODE"
        private var addresFrom: Address? = null
        private var addresTo: Address? = null
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private val geoCodingType = GeoCodingType.OpenStreetMap
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