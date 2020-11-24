package com.sedi.routelist.backgrounds

import android.location.Location
import com.huawei.hms.maps.model.LatLng

object LocationManager {
    private var lastLocation = LatLng(42.87, 74.59)

    fun getLastLocation() = lastLocation

    fun setLastLocation(latLng: LatLng) {
        lastLocation = latLng
    }
}