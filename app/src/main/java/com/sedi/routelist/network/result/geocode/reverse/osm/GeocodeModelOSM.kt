package com.sedi.routelist.network.result.geocode.reverse.osm

data class GeocodeModelOSM(
    val address: Address,
    val boundingbox: List<String>,
    val `class`: String,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val osm_id: Int,
    val osm_type: String,
    val place_id: Int,
    val type: String
)

class AddressesOSM : ArrayList<GeocodeModelOSM>()

data class Address(
    val country: String,
    val country_code: String,
    val county: String,
    val house_number: String,
    val municipality: String,
    val postcode: String,
    val state: String,
    val village: String
)