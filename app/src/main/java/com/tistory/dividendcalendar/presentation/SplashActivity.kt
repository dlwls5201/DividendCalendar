package com.tistory.dividendcalendar.presentation

import android.content.Intent
import android.os.Bundle
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseActivity
import com.tistory.dividendcalendar.databinding.ActivitySplashBinding
import com.tistory.dividendcalendar.presentation.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}