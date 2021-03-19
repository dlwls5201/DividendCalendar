package com.tistory.dividendcalendar.presentation.searchstock

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tistory.data.repository.StockNameRepository
import com.tistory.dividendcalendar.presentation.searchstock.adapter.StockNameItem
import kotlinx.coroutines.launch

class SearchStockViewModel @ViewModelInject constructor(
    private val stockNameRepository: StockNameRepository
) : ViewModel() {

    val editSearchText = MutableLiveData<String>()

    val stockNames = MediatorLiveData<List<StockNameItem>>().apply {
        addSource(editSearchText) { query ->
            viewModelScope.launch {
                val items = stockNameRepository.getStocksFromQuery(query).map { entity ->
                    StockNameItem(
                        query = query,
                        ticker = entity.ticker,
                        companyName = entity.companyName
                    )
                }
                postValue(items)
            }
        }
    }
}