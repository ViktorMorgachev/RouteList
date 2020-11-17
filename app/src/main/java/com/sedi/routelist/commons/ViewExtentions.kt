package com.sedi.routelist.commons

import android.app.Activity
import android.view.View
import android.widget.Toast

fun showToast(activity: Activity, text: String, duration: Int = Toast.LENGTH_LONG) {
    activity.runOnUiThread { Toast.makeText(activity, text, duration).show() }
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}