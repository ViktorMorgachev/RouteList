package com.sedi.routelist.commons

import android.os.AsyncTask
import java.text.SimpleDateFormat
import java.util.*

fun asynkExecute(action: (() -> Unit)) {
    AsyncTask.execute {
        Thread() {
            action()
        }.start()
    }
}

fun asynkExecutePostAction(action: (() -> Unit), postAction: (()->Unit)) {
    AsyncTask.execute {
        Thread() {
            action()
            postAction()
        }.start()
    }
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    return sdf.format(Date())
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("hh:mm", Locale.ROOT)
    return sdf.format(Date())
}