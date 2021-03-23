package com.tistory.dividendcalendar.presentation.notice

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tistory.blackjinbase.ext.alert
import com.tistory.blackjinbase.ext.dialog.cancelButton
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.BuildConfig
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.DividendFragment
import com.tistory.dividendcalendar.constant.Constant
import com.tistory.dividendcalendar.databinding.FragmentNoticeBinding
import com.tistory.dividendcalendar.presentation.notice.adapter.NoticeAdapter
import com.tistory.dividendcalendar.presentation.notice.model.mapToItem
import com.tistory.domain.model.NoticeResponse
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NoticeFragment : DividendFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {

    companion object {

        fun newInstance() = NoticeFragment()
    }

    override var logTag = "NoticeFragment"

    private val noticeAdapter by lazy { NoticeAdapter() }

    private val remoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            if (BuildConfig.DEBUG) {
                minimumFetchIntervalInSeconds = 60
            }
        }
        setConfigSettingsAsync(configSettings)
        setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initButton()
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

    private fun initButton() {
        binding.fabSendEmail.setOnClickListener {
            val info: PackageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            val versionName = "ver. ${info.versionName}"

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
            val address = arrayOf("baedanggum@gmail.com")
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
        showLoading()
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                hideLoading()
                if (task.isSuccessful) {

                    val noticeItemsJsonString = remoteConfig.getString("notice_items")
                    Dlog.d("noticeItemsJsonString : $noticeItemsJsonString")

                    try {
                        val noticeResponse =
                            convertJsonArrayToList<NoticeResponse>(noticeItemsJsonString)

                        val response = findNoticeForDisplayLanguage(noticeResponse)
                        noticeAdapter.replaceAll(response.items.map { it.mapToItem() })

                    } catch (e: Exception) {
                        toast("${e.message}")
                    }

                } else {
                    toast("Fetch failed")
                }
            }
    }

    private inline fun <reified T> convertJsonArrayToList(jsonString: String): List<T> {
        return gson.fromJson(
            jsonString,
            TypeToken.getParameterized(ArrayList::class.java, T::class.java).type
        )
    }

    private fun findNoticeForDisplayLanguage(notices: List<NoticeResponse>): NoticeResponse {
        val language = Locale.getDefault().language.toLowerCase(Locale.ROOT)
        Dlog.d("language : $language")

        return notices.find { it.country == language }
            ?: notices.find { it.country == Constant.DEFAULT_LANGUAGE }
            ?: NoticeResponse()
    }

    private fun showLoading() {
        binding.pbNotice.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbNotice.visibility = View.GONE
    }
}