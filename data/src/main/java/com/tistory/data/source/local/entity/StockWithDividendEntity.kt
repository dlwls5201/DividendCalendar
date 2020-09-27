package com.tistory.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tistory.data.source.remote.model.DividendResponse
import com.tistory.data.source.remote.model.LogoResponse
import com.tistory.data.source.remote.model.ProfileResponse

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey
    val symbol: String,

    val stockCnt: Int = 0,
    val logoUrl: String = "",
    val companyName: String = "",
    val exchange: String = "",
    val industry: String = "",
    val website: String = "",
    val description: String = "",
    val ceo: String = "",
    val securityName: String = "",
    val issueType: String = "",
    val sector: String = "",
    val primarySicCode: Int = 0,
    val employees: Int = 0,
    val address: String = "",
    val state: String = "",
    val city: String = "",
    val zip: String = "",
    val country: String = "",
    val phone: String = ""
) {

    companion object {
        fun create(
            symbol: String,
            stockCnt: Int,
            logo: LogoResponse,
            profileResponse: ProfileResponse
        ) =
            StockEntity(
                symbol = symbol,

                stockCnt = stockCnt,
                logoUrl = logo.url,
                companyName = profileResponse.companyName,
                exchange = profileResponse.exchange,
                industry = profileResponse.industry,
                website = profileResponse.website,
                description = profileResponse.description,
                ceo = profileResponse.ceo,
                securityName = profileResponse.securityName,
                issueType = profileResponse.securityName,
                sector = profileResponse.sector,
                primarySicCode = profileResponse.primarySicCode,
                employees = profileResponse.employees,
                address = profileResponse.address,
                state = profileResponse.state,
                city = profileResponse.city,
                zip = profileResponse.zip,
                country = profileResponse.country,
                phone = profileResponse.phone
            )
    }
}

@Entity(tableName = "dividends")
data class DividendEntity(
    @PrimaryKey
    val declaredDate: String,
    val parentSymbol: String,

    val exDate: String = "",
    val paymentDate: String = "",
    val recordDate: String = "",
    val amount: Float = 0f,
    val frequency: String = "",
    val requestedDate: Long = 0L
) {

    companion object {
        fun create(
            symbol: String,
            dividend: DividendResponse
        ) = DividendEntity(
            declaredDate = "${symbol}_${dividend.declaredDate}",
            parentSymbol = symbol,

            exDate = dividend.exDate,
            paymentDate = dividend.paymentDate,
            recordDate = dividend.recordDate,
            amount = dividend.amount.toFloat(),
            frequency = dividend.frequency,
            requestedDate = System.currentTimeMillis() / 1000
        )

        fun createDummy(
            symbol: String
        ) = DividendEntity(
            declaredDate = "${symbol}_dummy",
            parentSymbol = symbol,
            requestedDate = System.currentTimeMillis() / 1000
        )
    }
}

fun DividendEntity.mapToItem(stock: StockEntity) = DividendItem(
    ticker = stock.symbol,
    companyName = stock.companyName,
    logoUrl = stock.logoUrl,
    stockCnt = stock.stockCnt,

    exDate = exDate,
    paymentDate = paymentDate,
    recordDate = recordDate,
    declaredDate = declaredDate,
    amount = amount,
    frequency = Frequency.getFrequency(frequency)
)

data class StockWithDividendEntity(
    @Embedded val stock: StockEntity,

    @Relation(
        parentColumn = "symbol",
        entityColumn = "parentSymbol"
    )
    val dividends: List<DividendEntity>
)