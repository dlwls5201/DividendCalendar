package com.tistory.dividendcalendar

import android.app.Application

class DividendCalendarApplication : Application() {

    companion object {
        lateinit var INSTANCE: Application
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}