package com.sedi.routelist.network.geocode.reverse

data class ReverseGeocode(
    val returnCode: String,
    val returnDesc: String,
    val sites: List<Site>
)