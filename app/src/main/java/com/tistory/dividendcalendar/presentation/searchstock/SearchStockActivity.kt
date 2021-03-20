package com.tistory.dividendcalendar.presentation.searchstock

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.blackjinbase.ext.observeEvent
import com.tistory.dividendcalendar.DividendActivity
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.constant.Constant
import com.tistory.dividendcalendar.databinding.ActivitySearchBinding
import com.tistory.dividendcalendar.firebase.DWFirebaseAnalyticsLogger
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockDialogFragment
import com.tistory.dividendcalendar.presentation.searchstock.adapter.SearchNameAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.userhabit.service.Userhabit

@AndroidEntryPoint
class SearchStockActivity : DividendActivity<ActivitySearchBinding>(R.layout.activity_search) {

    override var logTag = "SearchStockActivity"

    private val searchStockViewModel by viewModels<SearchStockViewModel>()

    private val searchStockAdapter by lazy {
        SearchNameAdapter().apply {
            onItemClickListener = {
                ModifyStockDialogFragment.newInstanceForAdd(it.ticker)
                    .show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.searchViewModel = searchStockViewModel
        initRecyclerView()
        sendLog()
    }

    override fun onViewModelSetup() {
        searchStockViewModel.stockNames.observe(this) {
            searchStockAdapter.replaceAll(it)
        }
        searchStockViewModel.eventShowAddDialog.observeEvent(this) {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        ModifyStockDialogFragment.newInstanceForAdd()
            .show(supportFragmentManager, null)
    }

    private fun sendLog() {
        Userhabit.setScreen(this, Constant.FB_VIEW_SEARCH_STOCK_ACTIVITY)
        DWFirebaseAnalyticsLogger.sendScreen(Constant.FB_VIEW_SEARCH_STOCK_ACTIVITY)
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
