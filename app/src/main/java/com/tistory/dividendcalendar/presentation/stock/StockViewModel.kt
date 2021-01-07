package com.tistory.dividendcalendar.presentation.stock

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tistory.dividendcalendar.DividendCalendarApplication
import com.tistory.dividendcalendar.R
import com.tistory.domain.model.StockWithDividendItem
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.flow.debounce

class StockViewModel(
    private val stockWithDividendRepository: StockWithDividendRepository
) : ViewModel() {

    val stockItems = stockWithDividendRepository.getStockItems().debounce(500).asLiveData()

    val topTitleLiveData = MediatorLiveData<String>().apply {
        addSource(stockItems) {
            val title = if (it.isEmpty()) {
                DividendCalendarApplication.INSTANCE.getString(R.string.app_name)
            } else {
                getDividendMonthlyTitle(it)
            }

            postValue(title)
        }
    }

    val isVisibleEmptyViewLiveData = MediatorLiveData<Boolean>().apply {
        addSource(stockItems) {
            postValue(it.isEmpty())
        }
    }

    private fun getDividendMonthlyTitle(items: List<StockWithDividendItem>): String {
        val amount = getDividendMonthly(items)
        return String.format(
            DividendCalendarApplication.INSTANCE.getString(R.string.total_dividend_monthly), amount
        )
    }

    private fun getDividendMonthly(items: List<StockWithDividendItem>): Float {
        var totalAmount = 0f

        items.forEach { item ->
            if (item.dividends.isNotEmpty()) {
                item.dividends.maxByOrNull { dividend -> dividend.paymentDate }?.also { dividend ->
                    totalAmount += dividend.amount * dividend.frequency.value * item.stockCnt
                }
            }
        }

        return totalAmount / 12
    }
}