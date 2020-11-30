package com.sedi.routelist.network

import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.enums.GeoCodingType
import com.sedi.routelist.enums.RouteType
import com.sedi.routelist.interfaces.IActionResult

interface IServices {
    fun getDirection(
        currentGeoCodingType: GeoCodingType,
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    )

    fun getAddress(geoCodingType: GeoCodingType, latLng: LatLng, iActionResult: IActionResult)
}