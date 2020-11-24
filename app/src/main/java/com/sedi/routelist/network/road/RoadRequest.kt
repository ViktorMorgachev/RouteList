package com.sedi.routelist.network.road

data class RoadRequest(
    val destination: Destination,
    val origin: Origin
)