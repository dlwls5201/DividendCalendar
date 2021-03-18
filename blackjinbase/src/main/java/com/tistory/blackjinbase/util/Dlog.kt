package com.tistory.blackjinbase.util

import android.util.Log

object Dlog {

    private const val TAG = "BlackJin"

    var isDebug = false

    fun initDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    // debug
    fun d(tag: String, msg: String?) {
        if (isDebug) {
            Log.d(tag, buildLogMsg(msg))
        }
    }

    fun d(msg: String?) {
        if (isDebug) {
            Log.d(TAG, buildLogMsg(msg))
        }
    }

    //info
    fun i(tag: String, msg: String?) {
        if (isDebug) {
            Log.i(tag, buildLogMsg(msg))
        }
    }

    fun i(msg: String?) {
        if (isDebug) {
            Log.i(TAG, buildLogMsg(msg))
        }
    }

    //warn
    fun w(tag: String, msg: String?) {
        if (isDebug) {
            Log.w(tag, buildLogMsg(msg))
        }
    }

    fun w(msg: String?) {
        if (isDebug) {
            Log.w(TAG, buildLogMsg(msg))
        }
    }

    //error
    fun e(tag: String, msg: String?) {
        if (isDebug) {
            Log.e(tag, buildLogMsg(msg))
        }
    }

    fun e(msg: String?) {
        if (isDebug) {
            Log.e(TAG, buildLogMsg(msg))
        }
    }

    //verbose
    fun v(tag: String, msg: String?) {
        if (isDebug) {
            Log.v(tag, buildLogMsg(msg))
        }
    }

    fun v(msg: String?) {
        if (isDebug) {
            Log.v(TAG, buildLogMsg(msg))
        }
    }

    //what a terrible failure
    fun wtf(tag: String, msg: String?) {
        if (isDebug) {
            Log.wtf(tag, buildLogMsg(msg))
        }
    }

    fun wtf(msg: String?) {
        if (isDebug) {
            Log.wtf(TAG, buildLogMsg(msg))
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