package com.tistory.dividendcalendar.utils

import android.content.Context
import android.content.SharedPreferences

object PrefUtil {

    const val PREF_NAME = "Pref"

    const val RECENT_LOADING_TIME = "recent_loading_time"

    const val ONCE_LOAD_STOCK_NAME = "once_load_stock_name"

    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun get(key: String, defValue: String): String {
        return pref.getString(key, defValue) ?: defValue
    }

    fun get(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun get(key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun get(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun put(key: String, value: String) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun put(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun put(key: String, value: Long) {
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun put(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun remove(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clear() {
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }
}