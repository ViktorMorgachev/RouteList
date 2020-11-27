package com.sedi.routelist.commons

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

fun checkLocationPermissions(context: Context, permissions: Array<String>): Boolean {
    permissions.forEach { permission ->
        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) return false
    }
    return true
}

fun getLocationPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

}

fun getInternetPermissions() = arrayOf(Manifest.permission.INTERNET)