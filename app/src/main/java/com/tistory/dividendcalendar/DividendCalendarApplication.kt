package com.tistory.dividendcalendar

import android.app.Application
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.di.Injection
import com.tistory.dividendcalendar.utils.PrefUtil

class DividendCalendarApplication : Application() {

    companion object {
        lateinit var INSTANCE: Application
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Injection.init(this)
        PrefUtil.init(this)
        Dlog.initDebug(BuildConfig.DEBUG)
    }
}