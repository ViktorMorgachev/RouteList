package com.sedi.routelist.commons

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
    permissions.forEach { _ ->
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        ) return false
    }
    return true
}

fun getLocationPermissions() = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

fun getInternetPermissions() = arrayOf(Manifest.permission.INTERNET)