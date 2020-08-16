package com.tistory.dividendcalendar.base.util

import android.util.Log
import com.tistory.dividendcalendar.BuildConfig


object Dlog {

    private const val TAG = "BlackJin"

    // debug
    fun d(tag: String = TAG, msg: String?) {
        if (isDebug()) {
            Log.d(tag, buildLogMsg(msg))
        }
    }

    //info
    fun i(tag: String = TAG, msg: String?) {
        if (isDebug()) {
            Log.i(tag, buildLogMsg(msg))
        }
    }

    //warn
    fun w(tag: String = TAG, msg: String?) {
        if (isDebug()) {
            Log.w(tag, buildLogMsg(msg))
        }
    }

    //error
    fun e(tag: String = TAG, msg: String?) {
        if (isDebug()) {
            Log.e(tag, buildLogMsg(msg))
        }
    }

    //verbose
    fun v(tag: String = TAG, msg: String?) {
        if (isDebug()) {
            Log.v(tag, buildLogMsg(msg))
        }
    }

    //what a terrible failure
    fun wtf(tag: String = TAG, msg: String?) {
        if (isDebug()) {
            Log.wtf(tag, buildLogMsg(msg))
        }
    }

    private fun isDebug(): Boolean {
        return BuildConfig.DEBUG
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