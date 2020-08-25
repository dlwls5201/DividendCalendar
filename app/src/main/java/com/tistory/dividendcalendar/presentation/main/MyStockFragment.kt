package com.tistory.dividendcalendar.presentation.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseFragment
import com.tistory.dividendcalendar.base.simplerecyclerview.SimpleRecyclerViewAdapter
import com.tistory.dividendcalendar.databinding.ItemStockBinding
import com.tistory.dividendcalendar.databinding.MyStockFragmentBinding
import com.tistory.dividendcalendar.presentation.main.model.StockModel

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

    class StockAdapter :
        SimpleRecyclerViewAdapter<StockModel, ItemStockBinding>(R.layout.item_stock, BR.data){

        fun showPopup(view: View) {
            var popup: PopupMenu? = null;
            popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.edit)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                when (item?.itemId) {
                    R.id.edit -> {
                        Toast.makeText(view.context, "", Toast.LENGTH_SHORT).show();
                    }
                    R.id.delete -> {
                        Toast.makeText(view.context, "", Toast.LENGTH_SHORT).show();
                    }
                }

                true
            })

            popup.show()
        }
    }
}