package com.sedi.routelist.commons

import android.animation.Animator
import android.app.Activity
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.sedi.routelist.R
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
    isClickable = false
    scaleX = 0f
    scaleY = 0f
    visible()
    ViewCompat.animate(this).setDuration(duration).scaleX(1f).scaleY(1f).start()
    AsyncTask.execute {
        Thread.sleep(duration + 100)
        post {
            isClickable = true
        }
    }
}

fun View.invisible(duration: Long = 500L) {
    isClickable = false
    animate().apply {
        scaleX(0f)
        scaleY(0f)
        setDuration(duration)
    }.start()

    AsyncTask.execute {
        Thread.sleep(duration + 100)
        post {
            isClickable = true
            invisible()
        }
    }
}

fun View.gone(duration: Long = 500L) {
    isClickable = false
    animate().apply {
        scaleX(0f)
        scaleY(0f)
        setDuration(duration)
    }.start()

    AsyncTask.execute {
        Thread.sleep(duration + 100)
        post {
            isClickable = true
            gone()
        }
    }
}

fun View.rotate180(duration: Long = 500L) {
    isClickable = false
    animate().apply {
        rotationBy(180f)
        rotation(0f)
        setDuration(duration)
    }.start()

    AsyncTask.execute {
        Thread.sleep(duration + 100)
        post {
            isClickable = true
        }
    }

}

fun View.rotate0(duration: Long = 500L) {
    isClickable = false
    animate().apply {
        rotationBy(0f)
        rotation(180f)
        setDuration(duration)
    }.start()

    AsyncTask.execute {
        Thread.sleep(duration + 100)
        post {
            isClickable = true
        }
    }

}