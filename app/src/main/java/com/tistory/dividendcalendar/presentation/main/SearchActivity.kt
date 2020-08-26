package com.tistory.dividendcalendar.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseActivity
import com.tistory.dividendcalendar.base.util.Dlog
import com.tistory.dividendcalendar.data.api.StockAPiImpl
import com.tistory.dividendcalendar.data.source.remote.ApiProvider
import com.tistory.dividendcalendar.databinding.ActivitySearchBinding
import com.tistory.dividendcalendar.databinding.ViewInputdialogBinding
import com.tistory.dividendcalendar.presentation.main.model.DividendsApiModel
import com.tistory.dividendcalendar.presentation.main.model.LogoApiModel
import com.tistory.dividendcalendar.presentation.main.model.StockApiModel
import com.tistory.dividendcalendar.presentation.main.model.StockModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 검색 화면
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {
    companion object {
        const val EXTRA_TICKER = "ticker"
        const val REQ_SEARCH = 4000
    }

    private lateinit var stockModelView: MyStockViewModel
    private lateinit var stockInfo: StockApiModel
    private lateinit var stockLogo: LogoApiModel
    private lateinit var stockDividends: DividendsApiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.activity = this
        binding.searchProgress.visibility = View.VISIBLE
        binding.searchMain.visibility = View.GONE

        stockModelView = ViewModelProvider(this).get(MyStockViewModel::class.java)

        intent?.extras?.let {
            val ticker = it.getString(EXTRA_TICKER)
            ticker?.let {
                Dlog.d("SJ", "ticker : ${it}")
                launch(Dispatchers.IO) {
                    val retrofit = ApiProvider.getRetrofitBuild()
                    val service = retrofit.create(StockAPiImpl::class.java)

                    val logo = async(Dispatchers.IO) {
                        service.companyLogo(ticker, ApiProvider.token)
                            .enqueue(object : Callback<LogoApiModel> {
                                override fun onResponse(
                                    call: Call<LogoApiModel>,
                                    response: Response<LogoApiModel>
                                ) {
                                    response.body()?.let { image ->
                                        stockLogo = image

                                        Glide.with(this@SearchActivity).load(image.url)
                                            .into(binding.logoImage)
                                    }
                                }

                                override fun onFailure(call: Call<LogoApiModel>, t: Throwable) {
                                    t.printStackTrace()
                                }
                            })
                    }

                    val companyInfo = async(Dispatchers.IO) {
                        service.companyInfo(ticker, ApiProvider.token)
                            .enqueue(object : Callback<StockApiModel> {
                                override fun onResponse(
                                    call: Call<StockApiModel>,
                                    response: Response<StockApiModel>
                                ) {
                                    response.body()?.run {
                                        stockInfo = this

                                        val desc = StringBuffer()
                                        desc.append("대표 : $CEO")
                                        desc.append("\n직원 수 : $employees")
                                        desc.append("\n주소 : $address")
                                        desc.append("\n도시 : $city")
                                        desc.append("\n전화 번호 : $phone")
                                        desc.append("\n웹사이트 : $website")
                                        desc.append("\n회사 소개\n$description")
                                        binding.desc.text = desc.toString()
                                    }
                                }

                                override fun onFailure(call: Call<StockApiModel>, t: Throwable) {

                                }
                            })
                    }

                    val dividends = async(Dispatchers.IO) {
                        service.companyDividends(ticker, "next", ApiProvider.token)
                            .enqueue(object : Callback<DividendsApiModel> {
                                override fun onResponse(
                                    call: Call<DividendsApiModel>,
                                    response: Response<DividendsApiModel>
                                ) {
                                    response.body()?.run {
                                        stockDividends = this

                                        val desc = StringBuffer()
                                        desc.append("배당 만료일 : $exDate")
                                        desc.append("\n배당 일자 : $paymentDate")
                                        desc.append("\n배당 기록일 : $recordDate")
                                        desc.append("\n배당 신고일 : $declaredDate")
                                        desc.append("\n배당 금액 : $amount")
                                        desc.append("\n배당 주기 : $frequency")
                                        binding.dividendsDesc.text = desc.toString()

                                    }
                                }

                                override fun onFailure(
                                    call: Call<DividendsApiModel>,
                                    t: Throwable
                                ) {

                                }
                            })
                    }

                    awaitAll(logo, companyInfo, dividends)
                    withContext(Dispatchers.Main) {
                        binding.searchProgress.visibility = View.GONE
                        binding.searchMain.visibility = View.VISIBLE
                    }
                }
            }
        }
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
            alertDialog.dismiss()

            if (view.inputCompany.text.toString()
                    .isEmpty() || view.inputInvestAmount.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "빈 곳을 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            launch(Dispatchers.IO) {
                var amount = 0
                try {
                    amount = stockDividends.amount.toInt()
                    amount *= view.inputInvestAmount.text.toString().toInt()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    amount = 0
                }

                val stockModel =
                    StockModel(
                        0,
                        view.inputCompany.text.toString(),
                        stockLogo.url,
                        stockInfo.symbol,
                        amount.toString()
                    )

                stockModelView.insert(stockModel)
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