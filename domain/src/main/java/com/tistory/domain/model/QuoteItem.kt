package com.tistory.domain.model

data class QuoteItem(
    val symbol: String = "",
    val companyName: String = "",
    val latestPrice: Double = 0.0,
    val week52High: Double = 0.0,
    val week52Low: Double = 0.0
)
