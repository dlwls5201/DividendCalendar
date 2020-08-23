package com.tistory.dividendcalendar.presentation.model

data class DividendItem(
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

enum class Range(val value: String) {
    NEXT("next"), MONTH("1m"), THREE_MONTH("3m"), SIX_MONTH("6m"),
    YEAR_TO_DATE("ytd"), ONE_YEAR("1y"), TWO_YEAR("2y"), FIVE_YEAR("5y"),

}