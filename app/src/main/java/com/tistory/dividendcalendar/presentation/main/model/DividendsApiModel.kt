package com.tistory.dividendcalendar.presentation.main.model

data class DividendsApiModel(
    val symbol: String = "",
    val exDate: String = "",
    val paymentDate: String = "",
    val recordDate: String = "",
    val declaredDate: String = "",
    val amount: String = "",
    val flag: String = "",
    val currency: String = "",
    val description: String = "",
    val frequency: String = ""
)
