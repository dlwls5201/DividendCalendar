package com.tistory.blackjinbase.sample

import android.os.Bundle
import com.tistory.blackjinbase.R
import com.tistory.blackjinbase.base.BaseActivity
import com.tistory.blackjinbase.databinding.ActivitySampleBinding
import com.tistory.blackjinbase.sample.base.RecyclerViewFragment
import com.tistory.blackjinbase.sample.simple.SimpleRecyclerViewFragment
import com.tistory.blackjinbase.sample.simpleasync.SimpleAsyncRecyclerViewFragment
import com.tistory.blackjinbase.sample.simplelist.SimpleListRecyclerViewFragment

class SampleActivity : BaseActivity<ActivitySampleBinding>(R.layout.activity_sample) {

    override var logTag = "SampleActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.lyt_sample_container, SampleFragment.newInstance())
            .commit()
    }

    fun goToRecyclerViewFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.lyt_sample_container, RecyclerViewFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    fun goToSimpleRecyclerViewFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.lyt_sample_container, SimpleRecyclerViewFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    fun goToSimpleAsyncRecyclerViewFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.lyt_sample_container, SimpleAsyncRecyclerViewFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    fun goToSimpleListRecyclerViewFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.lyt_sample_container, SimpleListRecyclerViewFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}
