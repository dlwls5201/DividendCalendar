package com.tistory.dividendcalendar.data.repository

import com.tistory.dividendcalendar.data.base.BaseResponse
import com.tistory.dividendcalendar.data.source.remote.model.ProfileResponse
import com.tistory.dividendcalendar.presentation.model.DividendItem

interface StockRepository {

    suspend fun getLogo(symbol: String, listener: BaseResponse<String>)

    suspend fun getProfile(symbol: String, listener: BaseResponse<ProfileResponse>)

    suspend fun getNextDividend(symbol: String, listener: BaseResponse<DividendItem>)

}