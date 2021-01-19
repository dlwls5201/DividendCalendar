package com.tistory.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockWithDividendItem(
    val symbol: String,
    val stockCnt: Float = 0f,
    val logoUrl: String = "",
    val companyName: String = "",
    val dividends: List<DividendItem>
) : Parcelable