package com.sedi.routelist.network.result.road.huawei

data class DirectionModel(
    val returnCode: String,
    val returnDesc: String,
    val routes: List<Route>
)

data class Bounds(
    val northeast: Northeast,
    val southwest: Southwest
)


data class EndLocation(
    val lat: Double,
    val lng: Double
)

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Path(
    val distance: Int,
    val duration: Int,
    val durationInTraffic: Int,
    val endLocation: EndLocation,
    val startLocation: StartLocation,
    val steps: List<Step>
)

data class Polyline(
    val lat: Double,
    val lng: Double
)

data class Route(
    val bounds: Bounds,
    val paths: List<Path>
)

data class Southwest(
    val lat: Double,
    val lng: Double
)

data class StartLocation(
    val lat: Double,
    val lng: Double
)

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