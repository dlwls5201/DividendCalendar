package com.tistory.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DividendItem(
    val ticker: String,
    val exDate: String = "",
    val paymentDate: String = "",
    val declaredDate: String = "",
    val amount: Float = 0f,
    val frequency: Frequency = Frequency.NONE
) : Parcelable