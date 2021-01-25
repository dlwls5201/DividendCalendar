package com.tistory.dividendcalendar.presentation.notice

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentNoticeBinding
import com.tistory.dividendcalendar.presentation.notice.adapter.NoticeAdapter

class NoticeFragment : DividendFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {

    companion object {

        fun newInstance() = NoticeFragment()
    }

    override var logTag = "NoticeFragment"

    private val noticeAdapter by lazy { NoticeAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        with(binding.rvNotice) {
            adapter = noticeAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun loadData() {
        //noticeAdapter.replaceAll()
    }
}