package com.sedi.routelist

import android.app.Activity
import android.app.Application
import android.os.AsyncTask
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.models.NoticeItemDatabase
import com.sedi.routelist.models.PrefsManager
import java.util.*

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

    public fun updateLocale() {
        try {
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            val localeCode: String =
                PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.LOCALE, "ru") as String
            conf.locale = Locale(localeCode)
            res.updateConfiguration(conf, dm)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}