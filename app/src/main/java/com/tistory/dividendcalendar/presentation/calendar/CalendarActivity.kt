package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.util.Dlog
import com.tistory.dividendcalendar.data.injection.Injection
import com.tistory.dividendcalendar.presentation.calendar.ext.showStockDialog
import kotlinx.android.synthetic.main.activity_calendar.*

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
        fabCalendarAddCompany.setOnClickListener {
            showStockDialog { ticker, stockCnt ->
                Dlog.d("ticker : $ticker , stockCnt : $stockCnt")
            }.show()
        }
    }

    private fun loadDividend() {
        /*GlobalScope.launch(Dispatchers.Main) {
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
        }*/
    }
}