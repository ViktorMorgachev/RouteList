package com.sedi.routelist.commons

import android.util.Log
import com.sedi.routelist.BuildConfig

enum class LOG_LEVEL {
    INFO, ERROR
}

fun log(log_level: LOG_LEVEL, message: String) {
    if (!BuildConfig.DEBUG) return
    var tag = "RouteList"
    when (log_level) {
        LOG_LEVEL.INFO -> Log.i(tag, message)
        LOG_LEVEL.ERROR -> Log.e(tag, message)
    }

}