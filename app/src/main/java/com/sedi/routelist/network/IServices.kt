package com.sedi.routelist.network

import androidx.databinding.ObservableField
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.interfaces.IActionResult
import com.sedi.routelist.network.result.road.huawei.DirectionModel
import java.util.*

interface IServices {
    fun getDirection(
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    )

    fun getAddress(geoCodingType: GeoCodingType, latLng: LatLng, iActionResult: IActionResult)
}