package com.tistory.dividendcalendar.presentation.stock

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tistory.domain.repository.StockWithDividendRepository

class StockViewModel @ViewModelInject constructor(
    stockWithDividendRepository: StockWithDividendRepository
) : ViewModel() {

    //TODO using debounce make error in test
    //java.lang.AbstractMethodError: kotlinx.coroutines.test.internal.TestMainDispatcher.invokeOnTimeout
    //val stockItems = stockWithDividendRepository.getStockItems().debounce(500).asLiveData()
    val stockItems = stockWithDividendRepository.getStockItems().asLiveData()

    val isVisibleEmptyViewLiveData = MediatorLiveData<Boolean>().apply {
        addSource(stockItems) {
            postValue(it.isEmpty())
        }
    }

    fun getDividendMonthly(): Float {
        var totalAmount = 0f

        stockItems.value?.forEach { item ->
            if (item.dividends.isNotEmpty()) {
                item.dividends.maxByOrNull { dividend -> dividend.paymentDate }?.also { dividend ->
                    totalAmount += dividend.amount * dividend.frequency.value * item.stockCnt
                }
            }
        }

        return totalAmount / 12
    }
}