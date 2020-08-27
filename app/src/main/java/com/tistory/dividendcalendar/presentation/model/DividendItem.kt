package com.tistory.dividendcalendar.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DividendItem(
    val ticker: String = "",
    val companyName: String = "",
    val logoUrl: String = "",
    val stockCnt: Int = 0,

    val exDate: String = "",
    val paymentDate: String = "",
    val recordDate: String = "",
    val declaredDate: String = "",
    val amount: Float = 0f,
    val frequency: Frequency = Frequency.NONE
) : Parcelable

enum class Frequency {
    MONTHLY, QUARTER, SEMI, ANNUAL, NONE;

    companion object {

        fun getFrequency(frequency: String) = when (frequency) {
            "monthly" -> MONTHLY
            "quarterly" -> QUARTER
            "semi-annual" -> SEMI
            "annual" -> ANNUAL
            else -> NONE
        }
    }
}