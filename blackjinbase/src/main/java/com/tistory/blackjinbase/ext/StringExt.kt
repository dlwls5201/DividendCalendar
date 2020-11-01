package com.tistory.blackjinbase.ext

fun String.toFloatCheckFormat(): Float {
    return try {
        toFloat()
    } catch (e: NumberFormatException) {
        0f
    }
}