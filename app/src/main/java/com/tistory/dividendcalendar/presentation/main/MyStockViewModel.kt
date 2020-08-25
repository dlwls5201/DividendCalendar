package com.tistory.dividendcalendar.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.tistory.dividendcalendar.presentation.main.model.StockModel
import com.tistory.dividendcalendar.presentation.main.room.StockDao
import com.tistory.dividendcalendar.presentation.main.room.StockDatabase

class MyStockViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var stockDao: StockDao

    init {
        val database = Room.databaseBuilder(application, StockDatabase::class.java, "stock").build()
        stockDao = database.stockDao()
    }

    fun getAll(): LiveData<List<StockModel>> {
        return stockDao.getAll()
    }

    fun insert(stockModel: StockModel) {
        stockDao.insert(stockModel)
    }


}