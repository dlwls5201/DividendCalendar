package com.tistory.dividendcalendar.utils

import android.content.Context
import com.tistory.data.repository.StockNameRepository

object ReadTextFileUtil {

    private const val KEY_TICKER = 0

    private const val KEY_COMPANY = 1

    suspend fun insertTickerFromFile(context: Context, stockNameRepository: StockNameRepository, fileName: String) {
        val inputStream = context.assets.open(fileName)
        val strItems = inputStream.bufferedReader().use { it.readText() }
        val items = strItems.split("\n")

        items.forEach { item ->
            val tickerWithCompany = item.split(",")
            if (tickerWithCompany.size == 2) {
                val ticker = tickerWithCompany[KEY_TICKER]
                var company = tickerWithCompany[KEY_COMPANY]
                if (company.contains(".")) {
                    company = company.split(".").first()
                }
                stockNameRepository.insertStock(ticker, company)
            }
        }
    }
}