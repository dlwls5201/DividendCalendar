package com.tistory.dividendcalendar.presentation.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.di.Injection
import com.tistory.dividendcalendar.presentation.calendar.ext.showStockDialog
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, CalendarFragment.newInstance())
            .commit()

        initUiTest()
        initRepository()
    }

    private fun initUiTest() {
        fabCalendarAddCompany.setOnClickListener {
            showStockDialog { ticker, stockCnt ->
                Dlog.d("ticker : $ticker , stockCnt : $stockCnt")
                putTickerAndStockCnt(ticker, stockCnt)
            }.show()
        }
    }

    private val repository by lazy {
        Injection.provideStockRepository(this)
    }

    private fun putTickerAndStockCnt(ticker: String, stockCnt: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.putStock(ticker, stockCnt)
        }
    }

    private fun initRepository() {
        lifecycleScope.launch(Dispatchers.IO) {
            val stocks = repository.getStocks()
            Dlog.d("stocks : $stocks")
        }
    }
}