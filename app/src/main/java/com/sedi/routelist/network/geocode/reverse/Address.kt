package com.sedi.routelist.network.geocode.reverse

data class Address(
    val adminArea: String,
    val country: String,
    val countryCode: String,
    val subAdminArea: String,
    val tertiaryAdminArea: String
)