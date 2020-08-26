package com.tistory.dividendcalendar.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity


object AppUtils {

    fun showSoftKeyBoard(activity: Activity) {
        val view = activity.currentFocus
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun showSoftKeyBoard(fragmentActivity: FragmentActivity) {
        val view = fragmentActivity.currentFocus
        val imm =
            fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun hideSoftKeyBoard(activity: Activity) {
        val view = activity.currentFocus
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        view?.clearFocus()
    }

    fun hideSoftKeyBoard(fragmentActivity: FragmentActivity) {
        val view = fragmentActivity.currentFocus
        val imm =
            fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragmentActivity.currentFocus?.windowToken, 0)
        view?.clearFocus()
    }

    // dp -> pixel 단위로 변경
    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}