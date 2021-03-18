package com.tistory.blackjinbase.util

import android.util.Log

object Showlog {

    private const val TAG = "shw"

    // debug
    fun d(msg: String?) {
        if (Dlog.isDebug) {
            Log.d(TAG, buildLogMsg(msg))
        }
    }

    private fun buildLogMsg(msg: String?): String {
        val sb = StringBuilder()
        if (TAG.isNotEmpty()) {
            sb.append("[").append(TAG).append("] ")
        }

        if (msg.isNullOrEmpty().not()) {
            sb.append(msg)
        }

        val stackTraceElements = Thread.currentThread().stackTrace
        if (stackTraceElements.size >= 4) {
            val element = stackTraceElements[4]
            val fullClassName = element.fileName
            val lineNumber = element.lineNumber.toString()
            sb.append(" (").append(fullClassName).append(":").append(lineNumber).append(")")
        }

        return sb.toString()
    }
}