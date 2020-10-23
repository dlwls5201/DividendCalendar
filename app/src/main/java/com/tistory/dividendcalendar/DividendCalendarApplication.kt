package com.tistory.dividendcalendar

import android.app.Application
import com.tistory.dividendcalendar.di.Injection

class DividendCalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injection.init(this)
    }
}