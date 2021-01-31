package com.tistory.dividendcalendar.presentation.notice

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.blackjinbase.ext.alert
import com.tistory.blackjinbase.ext.dialog.cancelButton
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.BuildConfig
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.databinding.FragmentNoticeBinding
import com.tistory.dividendcalendar.presentation.notice.adapter.NoticeAdapter
import com.tistory.dividendcalendar.presentation.notice.model.NoticeItem
import com.tistory.domain.base.BaseListener
import com.tistory.domain.model.NoticeResponse
import com.tistory.domain.usecase.GetLatestVersionUsecase
import com.tistory.domain.usecase.GetNoticeUsecase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NoticeFragment : DividendFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {

    companion object {

        fun newInstance() = NoticeFragment()
    }

    override var logTag = "NoticeFragment"

    @Inject
    lateinit var getNoticeUsecase: GetNoticeUsecase

    @Inject
    lateinit var getLatestVersionUsecase: GetLatestVersionUsecase

    private val noticeAdapter by lazy { NoticeAdapter() }

    private var versionCodeFromServer = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initButton()
        loadData()
        loadLatestVersion()
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

    private fun initButton() {
        binding.fabSendEmail.setOnClickListener {
            val info: PackageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            val versionCode = info.versionCode
            val versionInfo = if (versionCode == versionCodeFromServer) {
                "(${getString(R.string.latest_version)})"
            } else {
                "(${getString(R.string.need_update)})"
            }

            val versionName = "ver. ${info.versionName} $versionInfo"

            requireContext().alert(title = versionName, message = getString(R.string.app_info)) {
                positiveButton(getString(R.string.question)) {
                    sendEmail()
                }
                cancelButton {
                    //..
                }
            }.show()
        }
    }

    private fun sendEmail() {
        val email = Intent(Intent.ACTION_SEND).apply {
            type = "plain/Text"
            val address = arrayOf("dlwls5201@gmail.com")
            putExtra(Intent.EXTRA_EMAIL, address)
            putExtra(Intent.EXTRA_SUBJECT, "<" + getString(R.string.app_name) + ">")
            putExtra(
                Intent.EXTRA_TEXT,
                "AppVersion :${BuildConfig.VERSION_NAME}\nDevice : ${Build.MODEL}\nAndroid OS : ${Build.VERSION.SDK_INT}\n\n Content :\n"
            )
        }
        startActivity(email)
    }

    private fun loadData() {
        lifecycleScope.launch {
            getNoticeUsecase.get(object : BaseListener<List<NoticeResponse.ItemResponse>>() {
                override fun onSuccess(data: List<NoticeResponse.ItemResponse>) {

                    val noticeItems = data.map { item ->
                        NoticeItem(
                            id = item.id,
                            title = item.title,
                            description = item.description,
                            updateDate = item.date
                        )
                    }

                    noticeAdapter.replaceAll(noticeItems)
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                    showLoading()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError error : ${error.message}")
                    toast(error.message)
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                    hideLoading()
                }

            })
        }
    }

    private fun showLoading() {
        binding.pbNotice.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbNotice.visibility = View.GONE
    }

    private fun loadLatestVersion() {
        lifecycleScope.launch {
            getLatestVersionUsecase.get(object : BaseListener<Int>() {
                override fun onSuccess(data: Int) {
                    versionCodeFromServer = data
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError error : ${error.message}")
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                }

            })
        }
    }
}