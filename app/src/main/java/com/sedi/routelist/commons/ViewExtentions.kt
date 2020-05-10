package com.sedi.routelist.commons

import android.app.Activity
import android.widget.Toast

fun showToast(activity: Activity, text: String, duration: Int = Toast.LENGTH_LONG) {
    activity.runOnUiThread { Toast.makeText(activity, text, duration).show() }
}