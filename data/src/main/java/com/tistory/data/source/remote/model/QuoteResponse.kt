package com.tistory.data.source.remote.model


import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("companyName")
    val companyName: String?,
    @SerializedName("latestPrice")
    val latestPrice: Double?,
    @SerializedName("latestTime")
    val latestTime: String?,
    @SerializedName("week52High")
    val week52High: Double?,
    @SerializedName("week52Low")
    val week52Low: Double?,
)
