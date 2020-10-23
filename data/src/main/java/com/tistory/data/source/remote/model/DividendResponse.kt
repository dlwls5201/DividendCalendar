package com.tistory.data.source.remote.model

data class DividendResponse(
    val exDate: String = "",
    val paymentDate: String = "",
    val recordDate: String = "",
    val declaredDate: String = "",
    val amount: String = "",
    val flag: String = "",
    val currency: String = "",
    val description: String = "",
    val frequency: String = "",  // monthly, quarterly, semi-annual, annual, unspecified
    val date: String = "" // 2020-10-23
)