package com.tistory.dividendcalendar.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tistory.blackjinbase.base.BaseActivity
import com.tistory.blackjinbase.ext.toast
import com.tistory.blackjinbase.util.Dlog
import com.tistory.data.source.local.entity.StockEntity
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ActivitySearchBinding
import com.tistory.dividendcalendar.databinding.ViewInputdialogBinding
import com.tistory.dividendcalendar.injection.Injection
import com.tistory.dividendcalendar.repository.base.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 검색 화면
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    override var logTag = "SearchActivity"

    companion object {
        const val EXTRA_TICKER = "ticker"
        const val REQ_SEARCH = 4000
    }

    private val stockRepository by lazy {
        Injection.provideStockRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.activity = this

        getStockFromIntent()?.let { ticker ->
            lifecycleScope.launch(Dispatchers.Main) {
                stockRepository.getStock(ticker, object : BaseResponse<StockEntity> {
                    override fun onSuccess(data: StockEntity) {
                        Dlog.d("onSuccess")
                        showSearchMain()

                        Glide.with(this@SearchActivity)
                            .load(data.logoUrl)
                            .into(binding.logoImage)

                        val desc = StringBuffer().apply {
                            append("대표 : ${data.ceo}")
                            append("\n직원 수 : ${data.employees}")
                            append("\n주소 : ${data.address}")
                            append("\n도시 : ${data.city}")
                            append("\n전화 번호 : ${data.phone}")
                            append("\n웹사이트 : ${data.website}")
                            append("\n회사 소개\n${data.description}")
                        }

                        binding.companyName.text = data.companyName
                        binding.desc.text = desc
                    }

                    override fun onFail(description: String) {
                        Dlog.d("onFail")
                        hideSearchMain()
                        toast(description)
                    }

                    override fun onError(throwable: Throwable) {
                        Dlog.d("onError")
                        hideSearchMain()
                        toast(throwable.message ?: "error")
                    }

                    override fun onLoading() {
                        Dlog.d("onLoading")
                        hideSearchMain()
                        showProgress()
                    }

                    override fun onLoaded() {
                        Dlog.d("onLoaded")
                        hideProgress()
                    }

                })
            }
        }
    }

    private fun getStockFromIntent() = intent?.extras?.getString(EXTRA_TICKER)

    private fun showProgress() {
        binding.searchProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.searchProgress.visibility = View.GONE
    }

    private fun showSearchMain() {
        binding.searchMain.visibility = View.VISIBLE
    }

    private fun hideSearchMain() {
        binding.searchMain.visibility = View.GONE
    }

    fun addCompany() {
        val view = DataBindingUtil.inflate<ViewInputdialogBinding>(
            LayoutInflater.from(this),
            R.layout.view_inputdialog,
            null,
            false
        )
        val dialog = AlertDialog.Builder(this)
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
                toast(getString(R.string.input_stock_cnt))
                return@setOnClickListener
            }

            alertDialog.dismiss()

            getStockFromIntent()?.let { ticker ->
                val stockCnt = view.inputInvestAmount.text.toString()
                lifecycleScope.launch(Dispatchers.Main) {
                    stockRepository.putStock(ticker, stockCnt.toInt(), object : BaseResponse<Any> {
                        override fun onSuccess(data: Any) {
                            Dlog.d("putStock onSuccess")
                            val intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
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
        }

        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}