package com.tistory.dividendcalendar.presentation.main.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tistory.dividendcalendar.presentation.main.model.StockModel

@Dao
interface StockDao {
    @Insert
    fun insert(stockModel: StockModel)

    @Update
    fun update(stockModel: StockModel)

    @Delete
    fun delete(stockModel: StockModel)

    @Query("SELECT * FROM StockModel")
    fun getAll(): LiveData<List<StockModel>>
}