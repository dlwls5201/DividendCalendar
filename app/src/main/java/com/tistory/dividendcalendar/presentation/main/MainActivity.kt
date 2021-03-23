package com.tistory.dividendcalendar.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tistory.blackjinbase.util.Dlog
import com.tistory.data.repository.StockNameRepository
import com.tistory.dividendcalendar.DividendActivity
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.constant.Constant
import com.tistory.dividendcalendar.databinding.ActivityMainBinding
import com.tistory.dividendcalendar.firebase.DWFirebaseAnalyticsLogger
import com.tistory.dividendcalendar.presentation.calendar.CalendarFragment
import com.tistory.dividendcalendar.presentation.notice.NoticeFragment
import com.tistory.dividendcalendar.presentation.searchstock.SearchStockActivity
import com.tistory.dividendcalendar.presentation.stock.StockFragment
import com.tistory.dividendcalendar.utils.PrefUtil
import com.tistory.dividendcalendar.utils.ReadTextFileUtil
import com.tistory.domain.base.BaseListener
import com.tistory.domain.usecase.RefreshAllStockDividendUsecase
import dagger.hilt.android.AndroidEntryPoint
import io.userhabit.service.Userhabit
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : DividendActivity<ActivityMainBinding>(R.layout.activity_main) {

    override var logTag = "MainActivity"

    private var stockFragment: StockFragment? = null
    private var calendarFragment: CalendarFragment? = null
    private var noticeFragment: NoticeFragment? = null

    @Inject
    lateinit var refreshAllStockDividendUsecase: RefreshAllStockDividendUsecase

    @Inject
    lateinit var stockNameRepository: StockNameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initButton()
        clearRemainedFragment()
        initStockFragment()
        checkOneDaySync()
        loadStockNamesOnce()
    }

    private fun loadStockNamesOnce() {
        val isOnce = PrefUtil.get(PrefUtil.ONCE_LOAD_STOCK_NAME, false)
        if (isOnce) {
            return
        }

        lifecycleScope.launch {
            try {
                stockNameRepository.clearStocks()
                ReadTextFileUtil.insertTickerFromFile(this@MainActivity, stockNameRepository, "amex_name.txt")
                ReadTextFileUtil.insertTickerFromFile(this@MainActivity, stockNameRepository, "nasdaq_name.txt")
                ReadTextFileUtil.insertTickerFromFile(this@MainActivity, stockNameRepository, "nyse_name.txt")
                PrefUtil.put(PrefUtil.ONCE_LOAD_STOCK_NAME, true)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private fun clearRemainedFragment() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            supportFragmentManager.fragments.forEach {
                supportFragmentManager.beginTransaction().remove(it).commit()
            }
        }
    }

    private fun checkOneDaySync() {
        val recentLoadingTime = PrefUtil.get(PrefUtil.RECENT_LOADING_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val diffTime = currentTime - recentLoadingTime

        val dayMilliSecond = 24 * 60 * 60 * 1000 // 86400000
        Dlog.d("diffTime : $diffTime -> dayMilliSecond : $dayMilliSecond")

        if (diffTime > dayMilliSecond) {
            syncAllStockWithDividends()
        }
    }

    private fun syncAllStockWithDividends() {
        lifecycleScope.launch {
            refreshAllStockDividendUsecase.get(object : BaseListener<Any>() {
                override fun onSuccess(data: Any) {
                    val currentTime = System.currentTimeMillis()
                    PrefUtil.put(PrefUtil.RECENT_LOADING_TIME, currentTime)
                }

                override fun onLoading() {
                    showFullProgress()
                }

                override fun onError(error: Throwable) {
                    Dlog.d("onError : ${error.message}")
                }

                override fun onLoaded() {
                    hideFullProgress()
                }
            })
        }
    }

    private fun initButton() {
        btnNavChart.setOnClickListener {
            Userhabit.setScreen(this, Constant.VIEW_STOCK_FRAGMENT)
            DWFirebaseAnalyticsLogger.sendScreen(Constant.FB_VIEW_STOCK_FRAGMENT)

            initStockFragment()
            showFloatingBtn()
        }

        btnNavCalendar.setOnClickListener {
            Userhabit.setScreen(this, Constant.VIEW_CALENDAR_FRAGMENT)
            DWFirebaseAnalyticsLogger.sendScreen(Constant.FB_VIEW_CALENDAR_FRAGMENT)

            initCalendarFragment()
            hideFloatingBtn()
        }

        btnNavSetting.setOnClickListener {
            Userhabit.setScreen(this, Constant.VIEW_NOTICE_FRAGMENT)
            DWFirebaseAnalyticsLogger.sendScreen(Constant.FB_VIEW_NOTICE_FRAGMENT)

            initNoticeFragment()
            hideFloatingBtn()
        }

        fabAddStock.setOnClickListener {
            startActivity(Intent(this, SearchStockActivity::class.java))
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

            noticeFragment?.let {
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

            noticeFragment?.let {
                beginTransaction().hide(it).commit()
            }
        }
        setNavIconsUnable()
        setNavIconEnable(ivNavCalendar)
    }

    private fun initNoticeFragment() {
        supportFragmentManager.run {
            stockFragment?.let {
                beginTransaction().hide(it).commit()
            }

            calendarFragment?.let {
                beginTransaction().hide(it).commit()
            }

            noticeFragment?.let {
                beginTransaction().show(it).commit()
            } ?: addNoticeFragment()
        }
        setNavIconsUnable()
        setNavIconEnable(ivNavNotice)
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

    private fun addNoticeFragment() {
        noticeFragment = NoticeFragment.newInstance().also {
            supportFragmentManager.beginTransaction()
                .add(R.id.flMainContainer, it).commit()
        }
    }

    private fun setNavIconsUnable() {
        ivNavChart.setColorFilter(ContextCompat.getColor(this, R.color.gray_02))
        ivNavCalendar.setColorFilter(ContextCompat.getColor(this, R.color.gray_02))
        ivNavNotice.setColorFilter(ContextCompat.getColor(this, R.color.gray_02))
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