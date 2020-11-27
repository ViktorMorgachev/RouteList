package com.sedi.routelist.ui

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import com.huawei.hms.site.api.model.Site
import com.huawei.hms.site.widget.SearchIntent
import com.sedi.routelist.MyApplication
import com.sedi.routelist.R
import com.sedi.routelist.adapters.ItemRoadType
import com.sedi.routelist.backgrounds.LocationManager
import com.sedi.routelist.commons.*
import com.sedi.routelist.enums.GeoCodingType
import com.sedi.routelist.enums.RouteType
import com.sedi.routelist.models.Address
import com.sedi.routelist.network.result.geocode.reverse.huawei.GeocodeModelHuawei
import com.sedi.routelist.network.result.road.huawei.DirectionModel
import com.sedi.routelist.interfaces.IActionResult
import com.sedi.routelist.network.NetService
import com.sedi.routelist.presenters.LocationPresenter
import com.sedi.routelist.presenters.RoutingPresenter
import kotlinx.android.synthetic.main.huawei_map_layout.*
import kotlinx.android.synthetic.main.item_center_map.*


class MapActivity : BaseActivity(), OnMapReadyCallback, HuaweiMap.OnCameraIdleListener,
    HuaweiMap.OnMapClickListener {

    private lateinit var hMap: HuaweiMap
    private var mapMode: Mode = Mode.GET_ROUTE
    private var currentAddress: Address? = null
    private val points: ArrayList<Marker> = arrayListOf()
    private lateinit var itemRoadType: ItemRoadType
    private val geoCodingType = GeoCodingType.OpenStreetMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.huawei_map_layout)
        var mapViewBundle: Bundle? = null


        if (intent.extras?.get(
                KEY_WORK_MODE
            ) == Mode.GET_POINT.name
        ) {
            mapMode = Mode.GET_POINT
            currentAddress = RememberData.remindMe(RememberData.KEYS.ADDRESS.value) as Address
        } else {
            mapMode = Mode.GET_ROUTE
        }

        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)

        MapsInitializer.setApiKey(resources.getString(R.string.api_key))
        mapView.onCreate(mapViewBundle)

        mapView.getMapAsync(this)
    }

    private fun init() {

        if (mapMode == Mode.GET_POINT) {
            panel_map_center.visible()
            panel_change_road.gone()
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
                setResult(RESULT_OK, intent)
                finish()
            }
        } else {
            panel_map_center.gone()
            panel_change_road.visible()

            itemRoadType = ItemRoadType(
                this,
                findViewById<ConstraintLayout>(R.id.change_road_root),
                object : ItemRoadType.ISelectedTypeListener {
                    override fun onTypeSelected(type: RouteType) {
                        RoutingPresenter.getDirections(type, addresFrom!!.location!!,
                            addresTo!!.location!!,
                            iActionResult = object : IActionResult {
                                override fun result(result: Any?, exception: java.lang.Exception?) {
                                    if (result != null) {
                                        if (result is DirectionModel) {
                                            log("Result: $result")
                                        }
                                    } else if (exception != null) {
                                        log(exception)
                                    }
                                }
                            })
                    }

                })
            itemRoadType.changeTittle(this, RoutingPresenter.currentRouteType)

        }



        if (checkLocationPermissions()) {
            LocationPresenter.initLocation(this)
            LocationPresenter.requestLocationUpdate()
        }

        if (mapMode == Mode.GET_POINT) {
            if (currentAddress != null) {

                log("CurrentAddress: $currentAddress")
                tv_address.text = currentAddress!!.address
                // Отключаем слушатель на время
                removeMapIdleListenerForTimeMilliss(600)
                if (currentAddress!!.location == null) {
                    onMapClick(LocationManager.getLastLocation())
                } else
                    onMapClick(currentAddress!!.location)
            }
        } else {
            log("AdressFrom: $addresFrom AddressTo: $addresTo")

            if (addresFrom != null && addresTo != null) {
                if (addresFrom!!.location != null) {
                    addPoint(addresFrom!!)
                    addPoint(addresTo!!)
                    onMapClick(addresFrom!!.location)
                    //onCameraBounds(addresFrom!!.location, addresTo!!.location)
                    onCameraMoveZoom(13f, addresFrom!!.location!!)
                    RoutingPresenter.getDirections(
                        RoutingPresenter.currentRouteType,
                        addresFrom!!.location!!,
                        addresTo!!.location!!,
                        iActionResult = object : IActionResult {
                            override fun result(result: Any?, exception: java.lang.Exception?) {
                                if (result != null) {
                                    if (result is DirectionModel) {
                                        log("Result: $result")
                                    }
                                } else if (exception != null) {
                                    log(exception)
                                }
                            }
                        })
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Activity.RESULT_OK && requestCode == KEY_REQUEST_LOCATION_PERMISSION) {
            LocationPresenter.initLocation(this)
            LocationPresenter.requestLocationUpdate()
        }
    }

    fun checkLocationPermissions(): Boolean {
        return if (!checkLocationPermissions(this, getLocationPermissions())) {
            ActivityCompat.requestPermissions(this, getLocationPermissions(), 1)
            false
        } else true
    }


    fun addPoint(address: Address) {
        var marker = points.find { it.position == address.location }
        marker?.let {
            marker!!.remove()
        }
        val options = MarkerOptions()
            .position(address.location)
            .title(address.address)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))

        marker = hMap.addMarker(options)

        if (!points.contains(marker)) {
            points.add(marker)
        }


    }


    override fun onMapReady(map: HuaweiMap) {
        //get map instance in a callback method

        hMap = map
        hMap.isMyLocationEnabled = true
        hMap.uiSettings.isMyLocationButtonEnabled = true
        hMap.setLanguage(MyApplication.language)
        hMap.setOnCameraIdleListener(this)
        hMap.setOnMapClickListener(this)
        hMap.setMarkersClustering(true)
        log("onMapReady")

        init()


    }

    private fun onCameraBounds(location: LatLng, location1: LatLng, padding: Int = 100) {

        if (location.latitude < location1.latitude) {
            hMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(
                        location,
                        location1
                    ), padding
                )
            )
        } else {
            hMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(
                        location1,
                        location
                    ), padding
                )
            )
        }

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
        val location = hMap.cameraPosition?.target

        if (location != null) {
            NetService.getAddress(
                geoCodingType,
                location,
                object : IActionResult {
                    override fun result(result: Any?, exception: Exception?) {
                        if (result != null) {
                            if (geoCodingType == GeoCodingType.HUAWEI) {
                                val data = result as GeocodeModelHuawei
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
                            if (!ll_address_info.isVisible)
                                ll_address_info.visible(500)
                        } else if (exception != null) {
                            log(exception)
                        }
                    }
                })
        }
    }


    override fun onMapClick(location: LatLng?) {
        if (location != null) {
            hMap.animateCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    private fun onCameraMoveZoom(zoom: Float, location: LatLng) {
        hMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            if (SearchIntent.isSuccess(resultCode)) {
                if (searchIntent != null) {
                    val site: Site = searchIntent!!.getSiteFromIntent(data)
                    currentAddress =
                        Address(
                            site.formatAddress,
                            LatLng(site.location.lat, site.location.lng)
                        )
                    log("currentAddress: $currentAddress")

                    LocationManager.setLastLocation(
                        LatLng(
                            site.location.lat,
                            site.location.lng
                        )
                    )
                    removeMapListener()
                    hMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                site.location.lat,
                                site.location.lng
                            ), 14f
                        ), object : HuaweiMap.CancelableCallback {
                            override fun onFinish() {
                                hMap.setOnCameraIdleListener(this@MapActivity)
                            }

                            override fun onCancel() {
                                hMap.setOnCameraIdleListener(this@MapActivity)
                            }
                        })
                }
            }
        }
    }

    private fun removeMapListener() {
        hMap.setOnCameraIdleListener { }
    }

    private fun removeMapIdleListenerForTimeMilliss(duration: Long = 600) {
        hMap.setOnCameraIdleListener { }
        AsyncTask.execute {
            Thread.sleep(duration)
            runOnUiThread {
                hMap.setOnCameraIdleListener(this)
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
        const val KEY_REQUEST_LOCATION_PERMISSION = 3
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