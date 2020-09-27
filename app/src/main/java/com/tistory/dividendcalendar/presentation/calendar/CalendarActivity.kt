package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.injection.Injection
import com.tistory.dividendcalendar.presentation.calendar.ext.showStockDialog
import com.tistory.dividendcalendar.repository.base.BaseResponse
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarActivity : AppCompatActivity() {

    private val stockRepository by lazy {
        Injection.provideStockRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, CalendarFragment.newInstance())
            .commit()
    }

    private fun initUiTest() {
        fabCalendarAddCompany.setOnClickListener {
            showStockDialog { ticker, stockCnt ->
                Dlog.d("ticker : $ticker , stockCnt : $stockCnt")
                putTickerAndStockCnt(ticker, stockCnt)
            }.show()
        }
    }

    private fun putTickerAndStockCnt(ticker: String, stockCnt: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            stockRepository.putStock(ticker, stockCnt, object : BaseResponse<Any> {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                }

                override fun onFail(description: String) {
                    Dlog.d("onFail")
                }

                override fun onError(throwable: Throwable) {
                    Dlog.d("onError")
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                }
            })
        }
    }
}