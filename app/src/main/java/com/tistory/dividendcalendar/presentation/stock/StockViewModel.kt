package com.tistory.dividendcalendar.presentation.stock

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tistory.domain.usecase.GetStockItemsUsecase

class StockViewModel @ViewModelInject constructor(
    getStockItemsUsecase: GetStockItemsUsecase
) : ViewModel() {

    val stockItems = getStockItemsUsecase.get().asLiveData()

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