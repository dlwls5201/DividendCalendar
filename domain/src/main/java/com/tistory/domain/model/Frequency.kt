package com.tistory.domain.model

enum class Frequency {
    MONTHLY, QUARTER, SEMI, ANNUAL, NONE;

    companion object {

        fun get(frequency: String) = when (frequency) {
            "monthly" -> MONTHLY
            "quarterly" -> QUARTER
            "semi-annual" -> SEMI
            "annual" -> ANNUAL
            else -> NONE
        }
    }
}