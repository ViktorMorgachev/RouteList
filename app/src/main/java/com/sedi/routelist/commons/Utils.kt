package com.sedi.routelist.commons

import android.app.Activity
import com.huawei.hms.maps.model.LatLng

fun checkNetworkConnectivity(activity: Activity): Boolean {
    return Device.isNetworkAvailable(activity)
}

fun emptyLocation() = LatLng(0.0, 0.0)