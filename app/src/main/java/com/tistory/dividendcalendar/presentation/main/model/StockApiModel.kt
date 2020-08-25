package com.tistory.dividendcalendar.presentation.main.model

data class StockApiModel(
    val symbol: String = "",
    val companyName: String = "",
    val exchange: String = "",
    val industry: String = "",
    val website: String = "",
    val description: String = "",
    val CEO: String = "",
    val securityName: String = "",
    val issueType: String = "",
    val sector: String = "",
    val primarySicCode: Int = 0,
    val employees: Int = 0,
    val tags: ArrayList<String> = ArrayList(),
    val address: String = "",
    val address2: String = "",
    val state: String = "",
    val city: String = "",
    val zip: String = "",
    val country: String = "",
    val phone: String = ""
)