package com.sedi.routelist

import android.app.Activity
import android.app.Application
import android.content.Context
import com.sedi.routelist.models.NoticeItemDatabase

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    fun getDB(activity: Activity) = NoticeItemDatabase.invoke(activity)
}