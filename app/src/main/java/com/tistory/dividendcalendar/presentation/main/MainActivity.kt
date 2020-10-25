package com.tistory.dividendcalendar.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.tistory.blackjinbase.base.BaseActivity
import com.tistory.blackjinbase.util.Dlog
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.databinding.ActivityMainBinding
import com.tistory.dividendcalendar.di.Injection
import com.tistory.dividendcalendar.presentation.calendar.CalendarFragment
import com.tistory.dividendcalendar.presentation.calendar.dialog.showStockDialog
import com.tistory.dividendcalendar.presentation.setting.SettingFragment
import com.tistory.dividendcalendar.presentation.stock.StockFragment
import com.tistory.domain.base.BaseListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override var logTag = "MainActivity"

    private var stockFragment: StockFragment? = null
    private var calendarFragment: CalendarFragment? = null
    private var settingFragment: SettingFragment? = null

    private val addStockUsecase by lazy {
        Injection.provideAddStockUsecase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initButton()
        initStockFragment()
    }

    private fun initButton() {
        btnNavChart.setOnClickListener {
            initStockFragment()
        }

        btnNavCalendar.setOnClickListener {
            initCalendarFragment()
        }

        btnNavSetting.setOnClickListener {
            initSettingFragment()
        }

        fabAddStock.setOnClickListener {
            showStockDialog(
                confirmListener = ::putStockWithDividend
            ).show()
        }
    }

    private fun initStockFragment() {
        supportFragmentManager.run {
            stockFragment?.let {
                beginTransaction().show(it).commit()
            } ?: addStockFragment()

            calendarFragment?.let {
                beginTransaction().hide(it).commit()
            }

            settingFragment?.let {
                beginTransaction().hide(it).commit()
            }
        }
        setNavIconsUnable()
        setNavIconEnable(ivNavChart)
    }

    private fun initCalendarFragment() {
        supportFragmentManager.run {
            stockFragment?.let {
                beginTransaction().hide(it).commit()
            }

            calendarFragment?.let {
                beginTransaction().show(it).commit()
            } ?: addCalendarFragment()

            settingFragment?.let {
                beginTransaction().hide(it).commit()
            }
        }
        setNavIconsUnable()
        setNavIconEnable(ivNavCalendar)
    }

    private fun initSettingFragment() {
        supportFragmentManager.run {
            stockFragment?.let {
                beginTransaction().hide(it).commit()
            }

            calendarFragment?.let {
                beginTransaction().hide(it).commit()
            }

            settingFragment?.let {
                beginTransaction().show(it).commit()
            } ?: addSettingFragment()
        }
        setNavIconsUnable()
        setNavIconEnable(ivNavSetting)
    }

    private fun addStockFragment() {
        stockFragment = StockFragment.newInstance().also {
            supportFragmentManager.beginTransaction()
                .add(R.id.flMainContainer, it).commit()
        }
    }

    private fun addCalendarFragment() {
        calendarFragment = CalendarFragment.newInstance().also {
            supportFragmentManager.beginTransaction()
                .add(R.id.flMainContainer, it).commit()
        }
    }

    private fun addSettingFragment() {
        settingFragment = SettingFragment.newInstance().also {
            supportFragmentManager.beginTransaction()
                .add(R.id.flMainContainer, it).commit()
        }
    }

    private fun setNavIconsUnable() {
        ivNavChart.setColorFilter(ContextCompat.getColor(this, R.color.gray_02))
        ivNavCalendar.setColorFilter(ContextCompat.getColor(this, R.color.gray_02))
        ivNavSetting.setColorFilter(ContextCompat.getColor(this, R.color.gray_02))
    }

    private fun setNavIconEnable(imageView: ImageView) {
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark))
    }

    private fun putStockWithDividend(ticker: String, stockCnt: Int) {
        lifecycleScope.launch {
            addStockUsecase.build(ticker, stockCnt, object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                    showProgress()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : ${error.message}")
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                    hideProgress()
                }
            })
        }
    }

    private fun showProgress() {
        flMainProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        flMainProgress.visibility = View.GONE
    }
}