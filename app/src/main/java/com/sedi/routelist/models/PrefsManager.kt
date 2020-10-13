package com.sedi.routelist.models

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PrefsManager {

    companion object {
        private var mSharedPreferences: SharedPreferences? = null
        private var instance: PrefsManager = PrefsManager()
        fun getIntance(context: Context): PrefsManager {
            if (mSharedPreferences == null) {
                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return instance
        }
    }

    fun setValue(key: PrefsKey, value: Comparable<*>) {
        val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
        when (value) {
            value is Int -> editor.putInt(key.name, value.toString().toInt())
            value is String -> editor.putString(key.name, value.toString())
            value is Long -> editor.putLong(key.name, value.toString().toLong())
            value is Boolean -> editor.putBoolean(key.name, value.toString().toBoolean())
            value is Float -> editor.putFloat(key.name, value.toString().toFloat())
        }
        editor.apply()
    }

    fun getValue(key: PrefsKey, defaultValue: Comparable<*>): Comparable<*>? {
        return when (defaultValue) {
            is Int -> mSharedPreferences?.getInt(
                key.name,
                defaultValue.toString().toInt()
            )
            is String -> mSharedPreferences?.getString(
                key.name,
                defaultValue.toString()
            )
            is Long -> mSharedPreferences?.getLong(
                key.name,
                defaultValue.toString().toLong()
            )
            is Boolean -> mSharedPreferences?.getBoolean(
                key.name,
                defaultValue.toString().toBoolean()
            )
            is Float -> mSharedPreferences?.getFloat(
                key.name,
                defaultValue.toString().toFloat()
            )
            else -> null
        }
    }


    enum class PrefsKey {
        FIRST_START, LOCALE
    }

}