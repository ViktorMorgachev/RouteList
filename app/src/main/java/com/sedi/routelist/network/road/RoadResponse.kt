package com.sedi.routelist.network.road

data class RoadResponse(
    val returnCode: String,
    val returnDesc: String,
    val routes: List<Route>
)