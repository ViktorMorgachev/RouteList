package com.sedi.routelist.commons

import android.animation.Animator
import android.app.Activity
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.route_list_fragment.*

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

fun View.visible(duration: Long = 500L) {
    alpha = 0f
    visible()
    ViewCompat.animate(this).setDuration(duration).alphaBy(1f).start()
}

fun View.invisible(duration: Long = 500L) {
    val view = this
    animate().apply {
        scaleXBy(0f)
        scaleYBy(0f)
        setDuration(duration)
    }.start()

    AsyncTask.execute {
        Thread.sleep(duration + 500)
        view.post {
            invisible()
        }
    }
}

fun View.gone(duration: Long = 500L) {
    val view = this
    animate().apply {
        scaleXBy(0f)
        scaleYBy(0f)
        setDuration(duration)
    }.start()

    AsyncTask.execute {
        Thread.sleep(duration + 500)
        view.post {
            gone()
        }
    }
}