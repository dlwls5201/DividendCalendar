package com.tistory.dividendcalendar.data.repository

import com.tistory.dividendcalendar.data.base.BaseResponse
import com.tistory.dividendcalendar.presentation.model.DividendItem
import com.tistory.dividendcalendar.presentation.model.ProfileItem

interface StockRepository {

    suspend fun getProfile(symbol: String, listener: BaseResponse<ProfileItem>)

    suspend fun getNextDividend(symbol: String, listener: BaseResponse<DividendItem>)

    suspend fun getDividendItems(listener: BaseResponse<List<DividendItem>>)
}