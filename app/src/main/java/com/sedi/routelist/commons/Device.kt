package com.sedi.routelist.commons

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build


object Device {

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw)
                actNw != null &&
                        (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo
                nwInfo != null && nwInfo.isConnected
            }
        }
        return false
    }


    fun getInfo(context: Context): String {
        val builder = StringBuilder()
        try {
            builder.append("Android: ").append(Build.VERSION.RELEASE)
            builder.append("|Device: ").append(Build.BRAND).append(" ").append(Build.MODEL)
            builder.append("|AppVersion: ")
                .append(context.packageManager.getPackageInfo(context.packageName, 0).versionName)
        } catch (e: Exception) {
            log(e)
        }
        return builder.toString()
    }
}