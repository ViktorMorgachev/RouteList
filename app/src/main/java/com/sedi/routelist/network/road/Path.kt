package com.sedi.routelist.network.road

data class Path(
    val distance: Int,
    val duration: Int,
    val durationInTraffic: Int,
    val endLocation: EndLocation,
    val startLocation: StartLocation,
    val steps: List<Step>
)