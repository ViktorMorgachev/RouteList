package com.sedi.routelist.commons

import android.app.Activity
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.network.geocode.reverse.Location

fun checkNetworkConnectivity(activity: Activity): Boolean {
    return Device.isNetworkAvailable(activity)
}

fun emptyLocation() = LatLng(0.0, 0.0)

fun locationToLatLng(location: Location) = LatLng(location.lat, location.lng)