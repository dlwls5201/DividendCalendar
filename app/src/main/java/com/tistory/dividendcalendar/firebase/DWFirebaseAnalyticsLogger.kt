package com.tistory.dividendcalendar.firebase

import android.app.Application
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockDialogFragment

object DWFirebaseAnalyticsLogger {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private const val VIEW_SCREEN = "screen"

    private const val STOCK_DIALOG_TYPE = "stockDialogType"
    private const val STOCK_TICKER = "ticker"
    private const val STOCK_COUNT = "count"

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

        val bundle = bundleOf(
            Pair(VIEW_SCREEN, screen)
        )

        try {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun changeStock(type: ModifyStockDialogFragment.DialogType, ticker: String, count: Float) {
        if (::firebaseAnalytics.isInitialized.not()) {
            return
        }

        val bundle = bundleOf(
            Pair(STOCK_DIALOG_TYPE, type),
            Pair(STOCK_TICKER, ticker),
            Pair(STOCK_COUNT, count)
        )

        try {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}