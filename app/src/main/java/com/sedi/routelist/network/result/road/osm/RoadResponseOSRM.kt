package com.sedi.routelist.network.result.road.osm

data class RoadResponseOSRM(
    val code: String,
    val routes: List<Route>,
    val waypoints: List<Waypoint>
)
data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)
data class Leg(
    val distance: Double,
    val duration: Double,
    val steps: List<Any>,
    val summary: String,
    val weight: Double
)

data class Route(
    val distance: Double,
    val duration: Double,
    val geometry: Geometry,
    val legs: List<Leg>,
    val weight: Double,
    val weight_name: String
)

data class Waypoint(
    val distance: Double,
    val hint: String,
    val location: List<Double>,
    val name: String
)