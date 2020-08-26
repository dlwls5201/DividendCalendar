package com.tistory.dividendcalendar.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.tistory.dividendcalendar.R
import com.tistory.dividendcalendar.base.BaseActivity
import com.tistory.dividendcalendar.databinding.ActivityMainBinding
import com.tistory.dividendcalendar.presentation.calendar.CalendarActivity


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var myStockFragment: MyStockFragment
    //private lateinit var calendarFragment: CalendarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUI()
    }

    private fun setUI() {
        binding.activity = this
        myStockFragment = MyStockFragment.newInstance()
        //calendarFragment = CalendarFragment.newInstance()

        supportFragmentManager.beginTransaction().add(R.id.mainFrame, myStockFragment)
            .commit()
        /* supportFragmentManager.beginTransaction().add(R.id.mainFrame, calendarFragment)
             .commit()*/
        //supportFragmentManager.beginTransaction().hide(calendarFragment).commit()
    }

    /**
     * MY STOCK, CALENDAR 탭 클릭시 리스너
     */
    fun onTab(view: View, isMyStock: Boolean) {
        if (isMyStock) {
            if (!myStockFragment.isHidden) return
            supportFragmentManager.beginTransaction().show(myStockFragment).commit()
            //supportFragmentManager.beginTransaction().hide(calendarFragment).commit()
        } else {
            //if (!calendarFragment.isHidden) return
            //supportFragmentManager.beginTransaction().hide(myStockFragment).commit()
            //supportFragmentManager.beginTransaction().show(calendarFragment).commit()
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
            mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // 검색화면으로 이동
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra(SearchActivity.EXTRA_TICKER, query)
                    startActivityForResult(intent, SearchActivity.REQ_SEARCH)

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
     * 앱 정보, 문의하기 클릭 이벤트
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSettingAppInfo -> {

            }
            R.id.actionSettingQna -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SearchActivity.REQ_SEARCH && resultCode == RESULT_OK) {


        }
    }
}