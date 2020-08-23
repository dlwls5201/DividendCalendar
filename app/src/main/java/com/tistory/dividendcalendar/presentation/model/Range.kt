package com.tistory.dividendcalendar.presentation.model

enum class Range(val value: String) {
    NEXT("next"), MONTH("1m"), THREE_MONTH("3m"), SIX_MONTH("6m"),
    YEAR_TO_DATE("ytd"), ONE_YEAR("1y"), TWO_YEAR("2y"), FIVE_YEAR("5y"),
}