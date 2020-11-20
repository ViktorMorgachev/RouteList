package com.sedi.routelist.commons

import android.app.Activity

fun checkNetworkConnectivity(activity: Activity): Boolean {
    return Device.isNetworkAvailable(activity)
}