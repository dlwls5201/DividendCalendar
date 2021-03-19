package com.tistory.dividendcalendar.presentation.searchstock

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.dividendcalendar.DividendActivity
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ActivitySearchBinding
import com.tistory.dividendcalendar.presentation.searchstock.adapter.SearchNameAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStockActivity : DividendActivity<ActivitySearchBinding>(R.layout.activity_search) {

    override var logTag = "SearchStockActivity"

    private val searchStockViewModel by viewModels<SearchStockViewModel>()

    private val searchStockAdapter by lazy {
        SearchNameAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.searchViewModel = searchStockViewModel
        initRecyclerView()
    }

    override fun onViewModelSetup() {
        searchStockViewModel.stockNames.observe(this) {
            searchStockAdapter.replaceAll(it)
        }
    }

    private fun initRecyclerView() {
        with(binding.rvTicker) {
            addItemDecoration(
                DividerItemDecoration(this@SearchStockActivity, LinearLayoutManager.VERTICAL).apply {
                    ContextCompat.getDrawable(this@SearchStockActivity, R.drawable.divider_line_gray)
                        ?.let {
                            setDrawable(it)
                        }
                }
            )
            adapter = searchStockAdapter
        }
    }
}
