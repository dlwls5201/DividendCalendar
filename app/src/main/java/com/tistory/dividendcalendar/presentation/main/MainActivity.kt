package com.tistory.dividendcalendar.presentation.main

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.autofill.AutofillManager
import androidx.appcompat.widget.SearchView
import com.tistory.dividendcalendar.BuildConfig
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseActivity
import com.tistory.dividendcalendar.base.ext.alert
import com.tistory.dividendcalendar.databinding.ActivityMainBinding
import com.tistory.dividendcalendar.presentation.calendar.CalendarActivity


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var myStockFragment: MyStockFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUI()
    }

    private fun setUI() {
        binding.activity = this
        myStockFragment = MyStockFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFrame, myStockFragment)
            .commit()
    }

    /**
     * MY STOCK, CALENDAR 탭 클릭시 리스너
     */
    fun onTab(view: View, isMyStock: Boolean) {
        if (isMyStock) {
            if (!myStockFragment.isHidden) return
            supportFragmentManager.beginTransaction().show(myStockFragment).commit()
        } else {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
    }

    /**
     * 검색, 설정 메뉴 추가
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.run {
            menuInflater.inflate(R.menu.menu, this)
            val mSearch: MenuItem = findItem(R.id.actionSearch)
            val mSearchView: SearchView = mSearch.actionView as SearchView
            mSearchView.queryHint = getString(R.string.searchHint)
            // API 11 autofill 기능사용
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mSearchView.setAutofillHints("ticker")
                //eventHandler(mSearchView)
            }
            mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // 검색화면으로 이동
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra(SearchActivity.EXTRA_TICKER, query)
                    startActivityForResult(intent, SearchActivity.REQ_SEARCH)

                    mSearchView.onActionViewCollapsed()

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * autofill 자동 완성 요청 강제
     * https://developer.android.com/guide/topics/text/autofill-optimize?hl=ko
     * https://github.com/googlearchive/android-AutofillFramework/tree/master/kotlinApp
     */
    fun eventHandler(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val afm = getSystemService(AutofillManager::class.java)
            afm?.requestAutofill(view)
        }
    }

    /**
     * 앱 정보, 문의하기 클릭 이벤트
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = info.versionName
        //val versionCode = info.versionCode

        when (item.itemId) {
            R.id.actionSettingAppInfo -> {
                val message = StringBuilder().apply {
                    append("내가 보유한 주식의 배당락일과 배당지급일을 달력을 통해 한 눈에 정리하여 확인할 수 있는 앱입니다.\n\n")
                    append("ver. $versionName")
                }
                alert(title = getString(R.string.app_name), message = message) {
                    positiveButton("확인") {}
                }.show()
            }
            R.id.actionSettingQna -> {
                sendEmail()
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SearchActivity.REQ_SEARCH && resultCode == RESULT_OK) {
            myStockFragment.calendarViewModel.loadDividendItems()
        }
    }
}