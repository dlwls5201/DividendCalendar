package com.tistory.dividendcalendar.presentation.model

data class DividendItem(
    val ticker: String = "",
    val companyName: String = "",
    val logoUrl: String = "",

    val exDate: String = "",
    val paymentDate: String = "",
    val recordDate: String = "",
    val declaredDate: String = "",
    val amount: Float = 0f,
    val frequency: Frequency = Frequency.NONE
)

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