package com.tistory.dividendcalendar.utils

import java.text.DecimalFormat

object CountUtil {

    private val df = DecimalFormat("#.###");

    @JvmStatic
    fun getDecimalFormat(n: Float): String {
        return df.format(n)
    }

    @JvmStatic
    fun getDecimalFormat(n: Double): String {
        return df.format(n)
    }
}