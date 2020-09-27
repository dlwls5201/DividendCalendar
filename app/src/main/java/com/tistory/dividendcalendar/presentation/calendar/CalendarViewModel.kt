package com.tistory.dividendcalendar.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.presentation.model.DividendItem
import com.tistory.dividendcalendar.repository.StockRepository
import com.tistory.dividendcalendar.repository.base.BaseResponse
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _dividendItems = MutableLiveData<List<DividendItem>>()
    val dividendItems: LiveData<List<DividendItem>> get() = _dividendItems

    fun loadDividendItems() {
        viewModelScope.launch {
            stockRepository.getAllDividendItems(object : BaseResponse<List<DividendItem>> {
                override fun onSuccess(data: List<DividendItem>) {
                    Dlog.d("onSuccess : $data")
                    _dividendItems.postValue(data)
                }

                override fun onFail(description: String) {
                    Dlog.d("onFail : $description")
                }

                override fun onError(throwable: Throwable) {
                    Dlog.d("onError : ${throwable.message}")
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