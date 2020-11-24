package com.sedi.routelist.network.road

data class Step(
    val action: String,
    val distance: Int,
    val duration: Int,
    val endLocation: EndLocation,
    val orientation: Int,
    val polyline: List<Polyline>,
    val roadName: String,
    val startLocation: StartLocation
)