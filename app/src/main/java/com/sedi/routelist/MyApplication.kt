package com.sedi.routelist

import android.app.Activity
import android.app.Application
import android.os.AsyncTask
import com.sedi.routelist.models.NoticeItemDatabase

class MyApplication : Application() {


     private lateinit var noticeItemDatabase: NoticeItemDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    fun initDB(activity: Activity) {
        noticeItemDatabase = NoticeItemDatabase.invoke(activity)
    }

    fun getDB() = noticeItemDatabase
}