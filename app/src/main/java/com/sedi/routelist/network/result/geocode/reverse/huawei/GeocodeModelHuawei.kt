package com.sedi.routelist.network.result.geocode.reverse.huawei

data class GeocodeModelHuawei(
    val returnCode: String,
    val returnDesc: String,
    val sites: List<Site>
)

data class Address(
    val adminArea: String,
    val country: String,
    val countryCode: String,
    val subAdminArea: String,
    val tertiaryAdminArea: String
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Poi(
    val hwPoiTypes: List<String>,
    val internationalPhone: String,
    val poiTypes: List<String>,
    val rating: Double
)

data class Site(
    val address: Address,
    val formatAddress: String,
    val location: Location,
    val name: String,
    val poi: Poi,
    val siteId: String,
    val viewport: Viewport
)


data class Southwest(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)