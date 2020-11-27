package com.sedi.routelist.enums

import com.sedi.routelist.R

enum class RouteType(val api: String, val textID: Int) {
    Bicycle("bicycling", R.string.bicycle),
    Drive("driving", R.string.drive),
    Walking("walking", R.string.walk)
}