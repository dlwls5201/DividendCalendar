package com.tistory.dividendcalendar.firebase

import android.app.Application
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tistory.dividendcalendar.constant.Constant
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockDialogFragment

object DWFirebaseAnalyticsLogger {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private const val STOCK_TICKER = "ticker"

    fun initLogger(application: Application) {
        try {
            firebaseAnalytics = FirebaseAnalytics.getInstance(application)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun sendScreen(screen: String) {
        if (::firebaseAnalytics.isInitialized.not()) {
            return
        }

        try {
            firebaseAnalytics.logEvent(screen, null)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun changeStock(type: ModifyStockDialogFragment.DialogType, ticker: String, count: Float) {
        if (::firebaseAnalytics.isInitialized.not()) {
            return
        }


        val key = when (type) {
            ModifyStockDialogFragment.DialogType.ADD -> Constant.FB_VIEW_STOCK_ADD_DIALOG
            ModifyStockDialogFragment.DialogType.MODIFY -> Constant.FB_VIEW_STOCK_MODIFY_DIALOG
        }

        val bundle = bundleOf(
            Pair(STOCK_TICKER, ticker)
        )

        try {
            firebaseAnalytics.logEvent(key, bundle)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}