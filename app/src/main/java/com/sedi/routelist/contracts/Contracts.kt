package com.sedi.routelist.contracts

import android.content.Context
import android.location.Location
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.enums.GeoCodingType
import com.sedi.routelist.enums.RouteType
import com.sedi.routelist.interfaces.IActionResult
interface ILocation {
    fun initLocation(context: Context)
    fun requestLocationUpdate()
    fun requestRemoveLocationUpdate()
    fun getLastKnownLocation(): Location?
}

interface IRouting {
    fun getDirections(
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    )
    fun repeatRequest()
}

interface IGeocode {
    fun reverseGeocode(geoCodingType: GeoCodingType, latLng: LatLng, iActionResult: IActionResult)
}