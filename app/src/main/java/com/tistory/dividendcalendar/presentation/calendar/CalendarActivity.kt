package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.ext.toast
import com.tistory.dividendcalendar.base.util.Dlog
import com.tistory.dividendcalendar.data.base.BaseResponse
import com.tistory.dividendcalendar.data.injection.Injection
import com.tistory.dividendcalendar.presentation.model.DividendItem
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarActivity : AppCompatActivity() {

    private val stockRepository by lazy {
        Injection.provideStockRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, CalendarFragment.newInstance())
            .commit()

        //TODO ui test
        initUiTest()
    }

    private fun initUiTest() {
        etTicker.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    // 검색 동작
                    loadDividend()
                    return@setOnEditorActionListener true
                }
                else -> {
                    return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun loadDividend() {
        //ticker는 전부 대문자로 통일합니다.
        val ticker = etTicker.text.toString().toUpperCase()
        Dlog.d("ticker : $ticker")

        if (ticker.isEmpty()) return

        GlobalScope.launch(Dispatchers.Main) {
            stockRepository.getNextDividend(ticker, object : BaseResponse<DividendItem> {
                override fun onSuccess(data: DividendItem) {
                    Dlog.d(msg = "data :$data")
                }

                override fun onFail(description: String) {
                    toast(description)
                }

                override fun onError(throwable: Throwable) {
                    Dlog.d(msg = "onError :${throwable.message}")
                }

                override fun onLoading() {
                    Dlog.d(msg = "onLoading")
                }

                override fun onLoaded() {
                    Dlog.d(msg = "onLoaded")
                }
            })
        }
    }
}