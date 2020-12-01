package com.sedi.routelist.presenters

import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.interfaces.IActionResult
import com.sedi.routelist.contracts.IRouting
import com.sedi.routelist.enums.GeoCodingType
import com.sedi.routelist.enums.RouteType
import com.sedi.routelist.network.NetService

object RoutingPresenter : IRouting {

    private var lastRequest: (() -> Unit)? = null
    var routeType = RouteType.Walking
        private set
    var geoCodingType = GeoCodingType.HUAWEI
        private set


    override fun getDirections(
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    ) {
        this.routeType = routeType
        this.routeType = routeType
        lastRequest = {
            NetService.getDirection(
                geoCodingType,
                routeType,
                latLngFrom,
                latLngTo,
                iActionResult
            )
        }
        lastRequest?.invoke()
    }

    fun changeGeoCodingType() {
        geoCodingType = if (geoCodingType == GeoCodingType.HUAWEI) {
            GeoCodingType.OpenStreetMap
        } else {
            GeoCodingType.HUAWEI
        }
    }

    fun setRouteType(routeType: RouteType) {
        this.routeType = routeType
    }

    fun repeatRequest() {
        lastRequest?.invoke()
    }
}