package com.sedi.routelist.presenters

import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.commons.log
import com.sedi.routelist.interfaces.IActionResult
import com.sedi.routelist.network.RouteType
import com.sedi.routelist.contracts.IRouting
import com.sedi.routelist.network.NetService
import com.sedi.routelist.network.result.road.huawei.DirectionModel

object RoutingPresenter: IRouting {

    override fun getDirections(
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    ) {
        NetService.getDirection(
            routeType,
            latLngFrom,
            latLngFrom,
            iActionResult)
    }
}