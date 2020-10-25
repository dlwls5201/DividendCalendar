package com.tistory.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockItem(
    val symbol: String,
    val stockCnt: Int = 0,
    val logoUrl: String = "",
    val companyName: String = "",
    val hasNextDividend: Boolean = false,
    val dividends: List<DividendItem>
) : Parcelable