package com.sedi.routelist.network.geocode.reverse.osm

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