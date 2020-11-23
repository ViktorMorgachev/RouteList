package com.sedi.routelist

import android.app.Activity
import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Build
import com.huawei.hms.maps.MapsInitializer
import com.sedi.routelist.models.NoticeItemDatabase
import com.sedi.routelist.models.PrefsManager
import java.util.*

class MyApplication : Application() {

    private lateinit var noticeItemDatabase: NoticeItemDatabase
    override fun onCreate() {
        super.onCreate()
        instance = this
        // Set the API key.
        if (PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.API_KEY, "").isEmpty())
            PrefsManager.getIntance(this)
                .setValue(PrefsManager.PrefsKey.API_KEY, resources.getString(R.string.api_key))

        language = PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.LOCALE, "en")
        api_key = PrefsManager.getIntance(this).getValue(PrefsManager.PrefsKey.API_KEY, "")
        MapsInitializer.setApiKey(api_key)
    }

    companion object {
        var language = ""
        var api_key = ""
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
            language = localeCode
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}