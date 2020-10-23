package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.di.Injection
import com.tistory.dividendcalendar.presentation.calendar.dialog.showStockDialog
import com.tistory.domain.base.BaseListener
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarActivity : AppCompatActivity() {

    private val addStockUsecase by lazy {
        Injection.provideAddStockUsecase()
    }

    private val repository by lazy {
        Injection.provideStockWithDividendRepo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, CalendarFragment.newInstance())
            .commit()

        initUiTest()
    }

    private fun initUiTest() {
        fabCalendarAddCompany.setOnClickListener {
            showStockDialog(
                confirmListener = ::putStockWithDividend
            ).show()
        }
    }

    private fun putStockWithDividend(ticker: String, stockCnt: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            addStockUsecase.build(ticker, stockCnt, object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : ${error.message}")
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                }
            })
        }
    }

    private fun fetchStock(ticker: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.fetchAndPutDividends(ticker)
        }
    }

    private fun deleteStock(ticker: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.deleteStock(ticker)
        }
    }

    private fun clearStock() {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.clearStock()
        }
    }
}