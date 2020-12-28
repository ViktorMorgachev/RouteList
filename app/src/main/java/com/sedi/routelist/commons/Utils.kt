package com.sedi.routelist.commons

import android.app.Activity
import android.content.Context
import android.location.Location
import android.widget.Toast
import com.huawei.hms.maps.model.LatLng
import java.time.Duration
import com.sedi.routelist.network.result.road.osm.Route as OSRMRoute
import com.sedi.routelist.network.result.road.huawei.Route as HuaweiRoute

fun checkNetworkConnectivity(activity: Activity): Boolean {
    return Device.isNetworkAvailable(activity)
}

fun emptyLocation() = LatLng(0.0, 0.0)


fun getPathList(route: HuaweiRoute): ArrayList<LatLng> {
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

fun getPathList(route: OSRMRoute): ArrayList<LatLng> {
    val pathList: ArrayList<LatLng> = arrayListOf()
    for (i in route.geometry.coordinates) {
        pathList.add(LatLng(i[0], i[1]))
    }
    return pathList
}

fun toast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}
