package com.sedi.routelist.network.request.road


data class RoadRequest(
    val destination: Destination,
    val origin: Origin
)

data class Origin(var lng: Double, var lat: Double)
data class Destination(var lng: Double, var lat: Double)