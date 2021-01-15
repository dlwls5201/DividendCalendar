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
import com.tistory.dividendcalendar.presentation.calendar.CalendarFragment
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockDialogFragment
import com.tistory.dividendcalendar.presentation.setting.SettingFragment
import com.tistory.dividendcalendar.presentation.stock.StockFragment
import com.tistory.dividendcalendar.utils.PrefUtil
import com.tistory.domain.base.BaseListener
import com.tistory.domain.usecase.RefreshAllStockDividendUsecase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override var logTag = "MainActivity"

    private var stockFragment: StockFragment? = null
    private var calendarFragment: CalendarFragment? = null
    private var settingFragment: SettingFragment? = null

    @Inject
    lateinit var refreshAllStockDividendUsecase: RefreshAllStockDividendUsecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initButton()
        initStockFragment()
        checkOneDaySync()
    }

    private fun checkOneDaySync() {
        val recentLoadingTime = PrefUtil.get(PrefUtil.RECENT_LOADING_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val diffTime = currentTime - recentLoadingTime

        val dayMilliSecond = 24 * 60 * 60 * 1000 // 86400000
        Dlog.d("diffTime : $diffTime -> dayMilliSecond :$dayMilliSecond")

        if (diffTime > dayMilliSecond) {
            syncAllStockWithDividends()
        }
    }

    private fun syncAllStockWithDividends() {
        lifecycleScope.launch {
            refreshAllStockDividendUsecase.build(object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    Dlog.d("onSuccess")
                    val currentTime = System.currentTimeMillis()
                    PrefUtil.put(PrefUtil.RECENT_LOADING_TIME, currentTime)
                }

                override fun onLoading() {
                    Dlog.d("onLoading")
                    showFullProgress()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : ${error.message}")
                }

                override fun onLoaded() {
                    Dlog.d("onLoaded")
                    hideFullProgress()
                }
            })
        }
    }

    private fun initButton() {
        btnNavChart.setOnClickListener {
            initStockFragment()
            showFloatingBtn()
        }

        btnNavCalendar.setOnClickListener {
            initCalendarFragment()
            hideFloatingBtn()
        }

        btnNavSetting.setOnClickListener {
            initSettingFragment()
            hideFloatingBtn()
        }

        fabAddStock.setOnClickListener {
            ModifyStockDialogFragment.newInstanceForAdd()
                .show(supportFragmentManager, logTag)
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

    private fun showFullProgress() {
        flMainContainer.visibility = View.GONE
        flMainProgress.visibility = View.VISIBLE
    }

    private fun hideFullProgress() {
        flMainContainer.visibility = View.VISIBLE
        flMainProgress.visibility = View.GONE
    }

    private fun showFloatingBtn() {
        fabAddStock.visibility = View.VISIBLE
    }

    private fun hideFloatingBtn() {
        fabAddStock.visibility = View.GONE
    }
}