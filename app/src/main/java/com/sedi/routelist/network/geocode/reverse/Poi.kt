package com.sedi.routelist.network.geocode.reverse

data class Poi(
    val hwPoiTypes: List<String>,
    val internationalPhone: String,
    val poiTypes: List<String>,
    val rating: Double
)