package com.tistory.dividendcalendar.utils

import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.doOnLayout

/**
 *  Android 11
 *  WindowInsets API
 *  개발자는 IME (소프트 키보드) 확장을 기반으로 애니메이션하거나 IME 확장을 제어하거나 단순히 IME 및 기타 삽입 유형의 가시성을 쿼리 할 수 ​​있습니다.
 */
//https://proandroiddev.com/android-11-creating-an-ime-keyboard-visibility-listener-c390a40d1ad0
//https://proandroiddev.com/exploring-windowinsets-on-android-11-a80cf8fe19be
@RequiresApi(Build.VERSION_CODES.R)
fun View.addKeyboardListener(keyboardCallback: (visible: Boolean) -> Unit) {
    doOnLayout {
        //get init state of keyboard
        var keyboardVisible = rootWindowInsets?.isVisible(WindowInsets.Type.ime()) == true

        //callback as soon as the layout is set with whether the keyboard is open or not
        keyboardCallback(keyboardVisible)

        //whenever the layout resizes/changes, callback with the state of the keyboard.
        viewTreeObserver.addOnGlobalLayoutListener {
            val keyboardUpdateCheck = rootWindowInsets?.isVisible(WindowInsets.Type.ime()) == true
            //since the observer is hit quite often, only callback when there is a change.
            if (keyboardUpdateCheck != keyboardVisible) {
                keyboardCallback(keyboardUpdateCheck)
                keyboardVisible = keyboardUpdateCheck
            }
        }
    }
}