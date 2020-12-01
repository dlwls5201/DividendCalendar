package com.tistory.domain.model

enum class Frequency(val value: Int) {
    MONTHLY(12), QUARTER(4), SEMI(2), ANNUAL(1), NONE(0);

    companion object {

        fun get(frequency: String?) = when (frequency) {
            "monthly" -> MONTHLY
            "quarterly" -> QUARTER
            "semi-annual" -> SEMI
            "annual" -> ANNUAL
            else -> NONE
        }
    }
}