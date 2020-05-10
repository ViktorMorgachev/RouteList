package com.sedi.routelist.commons

import android.os.AsyncTask
import android.text.Editable

fun asynkExecute(action: (() -> Unit)) {
    AsyncTask.execute {
        Thread() {
            action()
        }.start()
    }
}