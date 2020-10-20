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

    //------getValues-------//


    fun setValue(key: PrefsKey, value: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
        editor.putBoolean(key.name, value)
        editor.apply()
    }

    fun setValue(key: PrefsKey, value: String) {
        val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
        editor.putString(key.name, value)
        editor.apply()
    }

    fun setValue(key: PrefsKey, value: Float) {
        val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
        editor.putFloat(key.name, value)
        editor.apply()
    }


    fun setValue(key: PrefsKey, value: Int) {
        val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
        editor.putInt(key.name, value)
        editor.apply()
    }

    fun setValue(key: PrefsKey, value: Long = 0L) {
        val editor: SharedPreferences.Editor = mSharedPreferences!!.edit()
        editor.putLong(key.name, value)
        editor.apply()
    }

    //------getValues-------//

    fun getValue(key: PrefsKey, defaultValue: Int = 0): Int {
        return mSharedPreferences!!.getInt(
            key.name,
            defaultValue
        )
    }

    fun getValue(key: PrefsKey, defaultValue: String = ""): String {
        return mSharedPreferences!!.getString(
            key.name,
            defaultValue
        )!!
    }

    fun getValue(key: PrefsKey, defaultValue: Long = 0L): Long {
        return mSharedPreferences!!.getLong(
            key.name,
            defaultValue
        )
    }

    fun getValue(key: PrefsKey, defaultValue: Boolean = false): Boolean {
        return mSharedPreferences!!.getBoolean(
            key.name,
            defaultValue
        )
    }

    fun getValue(key: PrefsKey, defaultValue: Float = 0.0f): Float {
        return mSharedPreferences?.getFloat(
            key.name,
            defaultValue
        )!!
    }

    enum class PrefsKey {
        FIRST_START, LOCALE
    }

}