package com.sedi.routelist.commons

import android.util.Log
import com.sedi.routelist.BuildConfig
import java.lang.Exception

enum class LOG_LEVEL {
    INFO, ERROR
}

fun log(message: String, log_level: LOG_LEVEL = LOG_LEVEL.INFO) {
    if (!BuildConfig.DEBUG) return
    var tag = "RouteList"
    when (log_level) {
        LOG_LEVEL.INFO -> Log.i(tag, message)
        LOG_LEVEL.ERROR -> Log.e(tag, message)
    }

}

fun log(exception: Exception) {
    if (!BuildConfig.DEBUG) return
    var tag = "RouteList"
    val exceptionInfo = exception.message ?: exception.localizedMessage
    exceptionInfo?.let {
        log(exceptionInfo, LOG_LEVEL.ERROR)
    }

}