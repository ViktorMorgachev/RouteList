package com.sedi.routelist.enums

import com.sedi.routelist.R

enum class RouteType(val HuaweiApi: String,  val textID: Int,  val OSRMApi: String = HuaweiApi) {
    Bicycle("bicycling", R.string.bicycle),
    Drive("driving", R.string.drive),
    Walking("walking", R.string.walk)
}