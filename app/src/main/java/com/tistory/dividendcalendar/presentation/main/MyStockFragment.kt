package com.tistory.dividendcalendar.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseFragment
import com.tistory.dividendcalendar.base.ext.alert
import com.tistory.dividendcalendar.base.ext.longToast
import com.tistory.dividendcalendar.base.ext.toast
import com.tistory.dividendcalendar.base.simplerecyclerview.SimpleRecyclerViewAdapter
import com.tistory.dividendcalendar.base.simplerecyclerview.SimpleViewHolder
import com.tistory.dividendcalendar.base.util.Dlog
import com.tistory.dividendcalendar.data.base.BaseResponse
import com.tistory.dividendcalendar.data.injection.Injection
import com.tistory.dividendcalendar.databinding.ItemStockBinding
import com.tistory.dividendcalendar.databinding.MyStockFragmentBinding
import com.tistory.dividendcalendar.databinding.ViewInputdialogBinding
import com.tistory.dividendcalendar.presentation.calendar.CalendarViewModel
import com.tistory.dividendcalendar.presentation.calendar.CalendarViewModelFactory
import com.tistory.dividendcalendar.presentation.model.DividendItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyStockFragment : BaseFragment<MyStockFragmentBinding>(R.layout.my_stock_fragment) {

    companion object {
        fun newInstance() = MyStockFragment()
    }

    val calendarViewModel by lazy {
        ViewModelProvider(
            this, CalendarViewModelFactory(
                Injection.provideStockRepository()
            )
        ).get(CalendarViewModel::class.java)
    }

    private val stockRepository by lazy {
        Injection.provideStockRepository()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.stockList.adapter = StockAdapter()

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.stockList.layoutManager = layoutManager

        calendarViewModel.loadDividendItems()

        calendarViewModel.dividendItems.observe(viewLifecycleOwner, Observer {
            binding.stockList.adapter?.let { adapter ->
                (adapter as StockAdapter).replaceAll(it)
            }
        })
    }

    inner class StockAdapter :
        SimpleRecyclerViewAdapter<DividendItem, ItemStockBinding>(R.layout.item_stock, BR.data) {

        override fun onBindViewHolder(holder: SimpleViewHolder<ItemStockBinding>, position: Int) {
            super.onBindViewHolder(holder, position)

            // 로고 이미지 로딩
            holder.binding.data?.let { data ->
                Glide.with(holder.itemView.context).load(data.logoUrl)
                    .into(holder.binding.stockLogo)
            }
            // 메뉴 클릭시
            holder.binding.stockMore.setOnClickListener { view ->
                holder.binding.data?.let { data ->
                    showPopup(view, data)
                }
            }
        }

        /**
         * 수정, 삭제하는 팝업메뉴
         */
        private fun showPopup(view: View, data: DividendItem) {
            val popup: PopupMenu = PopupMenu(view.context, view)
            popup.inflate(R.menu.edit)
            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item?.itemId) {
                    R.id.edit -> {
                        editCompany(view, data)
                    }
                    R.id.delete -> {
                        //viewModel.delete(data)
                        context?.alert(title = "배담금 달력에 표시된 모든 배당금 정보가 사라집니다") {
                            positiveButton("삭제") {
                                launch(Dispatchers.Main) {
                                    stockRepository.deleteStockFromTicker(data.ticker, object :
                                        BaseResponse<Any> {
                                        override fun onSuccess(data: Any) {
                                            Dlog.d("onSuccess")
                                            calendarViewModel.loadDividendItems()
                                            view.context.toast("삭제 되었습니다.")
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

                            negativeButton("취소") { }
                        }?.show()
                    }
                    R.id.reload -> {
                        reloadData(data)
                    }
                }

                true
            }

            popup.show()
        }

        /**
         * 수량 수정하는 다이얼로그
         */
        private fun editCompany(moreView: View, data: DividendItem) {
            val view = DataBindingUtil.inflate<ViewInputdialogBinding>(
                LayoutInflater.from(moreView.context),
                R.layout.view_inputdialog,
                null,
                false
            )
            val dialog = AlertDialog.Builder(moreView.context)
            dialog.setView(view.root)
            dialog.setCancelable(false)

            val alertDialog = dialog.create()
            view.inputCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            view.inputConfirm.setOnClickListener {
                if (view.inputInvestAmount.text.toString()
                        .isEmpty() || view.inputInvestAmount.text.toString().toInt() <= 0
                ) {
                    context?.toast(getString(R.string.input_stock_cnt))
                    return@setOnClickListener
                }

                alertDialog.dismiss()

                launch(Dispatchers.Main) {
                    val stockCnt = view.inputInvestAmount.text.toString().toInt()
                    stockRepository.putStock(data.ticker, stockCnt, object : BaseResponse<Any> {
                        override fun onSuccess(data: Any) {
                            Dlog.d("onSuccess")
                            calendarViewModel.loadDividendItems()
                            context?.toast("수정 되었습니다.")
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

            alertDialog.show()
        }

        /**
         * 배당금 다시 받아오기
         */
        private fun reloadData(data: DividendItem) {
            launch(Dispatchers.Main) {
                stockRepository.loadNextDividendsFromTicker(
                    data.ticker,
                    object : BaseResponse<Any> {
                        override fun onSuccess(data: Any) {
                            context?.longToast(requireContext().getString(R.string.complete))
                            calendarViewModel.loadDividendItems()
                        }

                        override fun onFail(description: String) {
                        }

                        override fun onError(throwable: Throwable) {
                        }

                        override fun onLoading() {
                        }

                        override fun onLoaded() {
                        }
                    })
            }
        }
    }
}