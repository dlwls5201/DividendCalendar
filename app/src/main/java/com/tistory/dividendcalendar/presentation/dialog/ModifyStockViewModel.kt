package com.tistory.dividendcalendar.presentation.dialog

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.tistory.blackjinbase.util.Dlog
import com.tistory.blackjinbase.util.Event
import com.tistory.dividendcalendar.R
import com.tistory.domain.base.BaseListener
import com.tistory.domain.repository.ModifyStockCountUsecase
import com.tistory.domain.usecase.AddStockUsecase
import com.tistory.domain.usecase.DeleteStockUsecase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ModifyStockViewModel @ViewModelInject constructor(
    @ApplicationContext
    private val context: Context,
    private val addStockUsecase: AddStockUsecase,
    private val deleteStockUsecase: DeleteStockUsecase,
    private val modifyStockCountUsecase: ModifyStockCountUsecase,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _deleteVisible = MutableLiveData<Boolean>()
    val deleteVisible: LiveData<Boolean> = _deleteVisible

    private val _btnOkTitle = MutableLiveData<String>()
    val btnOkTitle: LiveData<String> get() = _btnOkTitle

    private val _btnOkLoading = MutableLiveData<Boolean>()
    val btnOkLoading: LiveData<Boolean> get() = _btnOkLoading

    val editTicker = MutableLiveData<String>()
    val editTickerEnable = MutableLiveData<Boolean>(true)
    val editTickerCnt = MutableLiveData<String>()

    val eventFinish = MutableLiveData<Event<Unit>>()
    val eventToast = MutableLiveData<Event<String>>()
    val eventHideKeyboard = MutableLiveData<Event<Unit>>()

    private val type by lazy {
        savedStateHandle.get(ModifyStockDialogFragment.ARGUMENT_TYPE) as? ModifyStockDialogFragment.DialogType
    }

    private val ticker by lazy {
        savedStateHandle.get(ModifyStockDialogFragment.ARGUMENT_TICKER) ?: ""
    }

    private val stockCnt by lazy {
        savedStateHandle.get(ModifyStockDialogFragment.ARGUMENT_STOCK_CNT) ?: 0
    }

    init {
        Dlog.d("type : $type , ticker : $ticker, stockCnt : $stockCnt")
        when (type) {
            ModifyStockDialogFragment.DialogType.ADD -> {
                hideDeleteButton()
                showBtnTitleAdd()
            }
            ModifyStockDialogFragment.DialogType.MODIFY -> {
                showDeleteButton()
                showBtnTitleModify()
                setDisableEditTicker()
            }
            null -> {
                finishView()
            }
        }
    }

    private fun showDeleteButton() {
        _deleteVisible.postValue(true)
    }

    private fun hideDeleteButton() {
        _deleteVisible.postValue(false)
    }

    private fun showBtnTitleAdd() {
        _btnOkTitle.postValue(context.getString(R.string.add))
    }

    private fun showBtnTitleModify() {
        _btnOkTitle.postValue(context.getString(R.string.modify))
    }

    private fun setDisableEditTicker() {
        editTicker.postValue(ticker)
        editTickerEnable.postValue(false)
        editTickerCnt.postValue(stockCnt.toString())
    }

    private fun showBtnOkLoading() {
        _btnOkLoading.postValue(true)
    }

    private fun hideBynOkLoading() {
        _btnOkLoading.postValue(false)
    }

    private fun showToastEvent(message: String?) {
        message?.let {
            eventToast.postValue(Event(it))
        }
    }

    private fun hideKeyboardEvent() {
        eventHideKeyboard.postValue(Event(Unit))
    }

    fun okStock() {
        val ticker = editTicker.value ?: ""
        val stockCount = editTickerCnt.value?.toIntOrNull() ?: 0
        Dlog.d("ticker : $ticker, stockCount : $stockCount")

        if (ticker.isEmpty()) {
            showToastEvent(context.getString(R.string.input_ticker))
            return
        }

        when (type) {
            ModifyStockDialogFragment.DialogType.ADD -> {
                addStock(ticker, stockCount)
            }
            ModifyStockDialogFragment.DialogType.MODIFY -> {
                modifyStockCount(ticker, stockCount)
            }
        }
    }

    private fun addStock(ticker: String, stockCnt: Int) {
        viewModelScope.launch {
            addStockUsecase.get(ticker, stockCnt, object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                    finishView()
                }

                override fun onLoading() {
                    showBtnOkLoading()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : ${error.message}")
                    if (error is HttpException && error.code() == 404) {
                        showToastEvent(context.resources.getString(R.string.not_find_selected_ticker))
                    } else {
                        showToastEvent(context.resources.getString(R.string.please_check_internet))
                    }
                }

                override fun onLoaded() {
                    hideBynOkLoading()
                    hideKeyboardEvent()
                }
            })
        }
    }

    private fun modifyStockCount(ticker: String, stockCnt: Int) {
        viewModelScope.launch {
            modifyStockCountUsecase.build(ticker, stockCnt, object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                    finishView()
                }

                override fun onLoading() {
                    //..
                }

                override fun onError(error: Throwable) {
                    showToastEvent(error.message)
                }

                override fun onLoaded() {
                    //..
                }
            })
        }
    }

    fun deleteStock() {
        viewModelScope.launch {
            deleteStockUsecase.build(ticker, object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                    finishView()
                }

                override fun onLoading() {
                    //..
                }

                override fun onError(error: Throwable) {
                    showToastEvent(error.message)
                }

                override fun onLoaded() {
                    //..
                }
            })
        }
    }

    fun finishView() {
        eventFinish.postValue(Event(Unit))
    }
}