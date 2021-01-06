package com.tistory.data.ext

fun String.toFloatCheckFormat(): Float {
    return try {
        toFloat()
    } catch (e: NumberFormatException) {
        0f
    }
}