package com.sedi.routelist.network.geocode.reverse

data class Site(
    val address: Address,
    val formatAddress: String,
    val location: Location,
    val name: String,
    val poi: Poi,
    val siteId: String,
    val viewport: Viewport
)