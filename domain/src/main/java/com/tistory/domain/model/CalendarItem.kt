package com.tistory.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarItem(
    val ticker: String = "",
    val companyName: String = "",
    val logoUrl: String = "",
    val stockCnt: Float = 0f,

    val exDate: String = "",
    val paymentDate: String = "",
    val declaredDate: String = "",

    val amount: Float = 0f,
    val frequency: Frequency = Frequency.NONE
) : Parcelable