package com.sedi.routelist.presenters

import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.interfaces.IActionResult
import com.sedi.routelist.network.RouteType
import com.sedi.routelist.contracts.IRouting

class RoutingPresenter(): IRouting {

    override fun getDirections(
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    ) {
        TODO("Not yet implemented")
    }
}