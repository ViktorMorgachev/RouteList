package com.sedi.routelist

import android.app.Activity
import android.app.Application
import android.os.AsyncTask
import com.sedi.routelist.models.NoticeItemDatabase
import com.sedi.routelist.models.PrefsManager

class MyApplication : Application() {


    private lateinit var noticeItemDatabase: NoticeItemDatabase


    override fun onCreate() {
        super.onCreate()
        instance = this

        // Если первый запуск
        if (PrefsManager.getIntance(applicationContext)
                .getValue(PrefsManager.PrefsKey.FIRST_START, true) as Boolean
        ) {
            // То показываем диалог выбора языка при первом старте
            PrefsManager.getIntance(applicationContext)
                .setValue(PrefsManager.PrefsKey.FIRST_START, true)
        }

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