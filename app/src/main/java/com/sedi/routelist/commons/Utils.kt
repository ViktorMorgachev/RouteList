package com.sedi.routelist.commons

import android.app.Activity
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.network.result.geocode.reverse.huawei.Location
import com.sedi.routelist.network.result.road.huawei.Route

fun checkNetworkConnectivity(activity: Activity): Boolean {
    return Device.isNetworkAvailable(activity)
}

fun emptyLocation() = LatLng(0.0, 0.0)

fun locationToLatLng(location: Location) = LatLng(location.lat, location.lng)

fun getPathList(route: Route): ArrayList<LatLng> {
    val pathList: ArrayList<LatLng> = arrayListOf()
    for (i in route.paths!!) {
        if (i.steps != null) {
            for (j in i.steps) {
                if (j.polyline != null && j.polyline.isNotEmpty()) {
                    for (k in j.polyline) {
                        pathList.add(
                            LatLng(
                                k.lat,
                                k.lng
                            )
                        )
                    }
                }
            }
        }
    }
    return pathList
}
