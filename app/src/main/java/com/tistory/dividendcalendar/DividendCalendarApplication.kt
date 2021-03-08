package com.tistory.dividendcalendar

import android.app.Application
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.firebase.DWFirebaseAnalyticsLogger
import com.tistory.dividendcalendar.utils.PrefUtil
import dagger.hilt.android.HiltAndroidApp
import io.userhabit.service.Userhabit

@HiltAndroidApp
class DividendCalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefUtil.init(this)
        Dlog.initDebug(BuildConfig.DEBUG)
        Userhabit.start(this)
        DWFirebaseAnalyticsLogger.initLogger(this)
    }
}