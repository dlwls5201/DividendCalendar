package com.tistory.dividendcalendar.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.tistory.dividendcalendar.base.simplerecyclerview.SimpleRecyclerViewAdapter
import com.tistory.dividendcalendar.base.simplerecyclerview.SimpleViewHolder
import com.tistory.dividendcalendar.databinding.ItemStockBinding
import com.tistory.dividendcalendar.databinding.MyStockFragmentBinding
import com.tistory.dividendcalendar.databinding.ViewInputdialogBinding
import com.tistory.dividendcalendar.presentation.main.model.StockModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyStockFragment : BaseFragment<MyStockFragmentBinding>(R.layout.my_stock_fragment) {

    companion object {
        fun newInstance() = MyStockFragment()
    }

    private lateinit var viewModel: MyStockViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyStockViewModel::class.java)
        binding.stockList.adapter = StockAdapter()

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.stockList.layoutManager = layoutManager

        binding.lifecycleOwner?.run {
            viewModel.getAll().observe(this, Observer {
                binding.stockList.adapter?.let { adapter ->
                    (adapter as StockAdapter).replaceAll(it)
                }
            })
        }
    }

    inner class StockAdapter :
        SimpleRecyclerViewAdapter<StockModel, ItemStockBinding>(R.layout.item_stock, BR.data) {

        override fun onBindViewHolder(holder: SimpleViewHolder<ItemStockBinding>, position: Int) {
            super.onBindViewHolder(holder, position)

            // 로고 이미지 로딩
            holder.binding.data?.let { data ->
                Glide.with(holder.itemView.context).load(data.companyLogo)
                    .into(holder.binding.stockLogo)
            }
            // 메뉴 클릭시
            holder.binding.stockMore.setOnClickListener { view ->
                holder.binding.data?.let { data ->
                    showPopup(view, data)
                }
            }
        }

        fun showPopup(view: View, data: StockModel) {
            var popup: PopupMenu? = null
            popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.edit)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                when (item?.itemId) {
                    R.id.edit -> {
                        editCompany(view, data)
                    }
                    R.id.delete -> {
                        viewModel.delete(data)
                        Toast.makeText(view.context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                true
            })

            popup.show()
        }

        fun editCompany(moreView: View, data: StockModel) {
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
                alertDialog.dismiss()

                if (view.inputInvestAmount.text.toString().isEmpty()) {
                    Toast.makeText(moreView.context, "빈 곳을 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                launch(Dispatchers.IO) {
                    data.amount = view.inputInvestAmount.text.toString().toInt().toString()
                    viewModel.update(data)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(moreView.context, "수정 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            alertDialog.show()
        }
    }
}