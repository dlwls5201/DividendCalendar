package com.tistory.dividendcalendar.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tistory.dividendcalendar.CoroutineTestRule
import com.tistory.dividendcalendar.getOrAwaitValue
import com.tistory.dividendcalendar.presentation.stock.StockViewModel
import com.tistory.domain.model.DividendItem
import com.tistory.domain.model.Frequency
import com.tistory.domain.model.StockWithDividendItem
import com.tistory.domain.repository.StockWithDividendRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


@ExperimentalCoroutinesApi
class StockViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var stockViewModel: StockViewModel

    private val stockWithDividendRepository = mock(StockWithDividendRepository::class.java)

    @Before
    fun setUp() {
        //..
    }

    @Test
    fun `has empty`() = coroutineTestRule.testDispatcher.runBlockingTest {
        //given
        `when`(stockWithDividendRepository.getStockItems()).thenReturn(
            flow { emit(emptyList<StockWithDividendItem>()) }
        )

        stockViewModel = StockViewModel(stockWithDividendRepository)

        //when
        stockViewModel.stockItems.getOrAwaitValue()

        //then
        val dividendMonthly = stockViewModel.getDividendMonthly()
        val visible = stockViewModel.isVisibleEmptyViewLiveData.getOrAwaitValue()

        Assert.assertEquals(0f, dividendMonthly)
        Assert.assertEquals(true, visible)
    }

    @Test
    fun `has one item`() = coroutineTestRule.testDispatcher.runBlockingTest {
        //given
        `when`(stockWithDividendRepository.getStockItems()).thenReturn(
            flow { emit(getItem()) }
        )
        stockViewModel = StockViewModel(stockWithDividendRepository)

        //when
        stockViewModel.stockItems.getOrAwaitValue()

        //then
        Assert.assertEquals(1200f, stockViewModel.getDividendMonthly())
        Assert.assertEquals(false, stockViewModel.isVisibleEmptyViewLiveData.getOrAwaitValue())
    }

    @Test
    fun `has items`() = coroutineTestRule.testDispatcher.runBlockingTest {
        //given
        `when`(stockWithDividendRepository.getStockItems()).thenReturn(
            flow { emit(getItems()) }
        )
        stockViewModel = StockViewModel(stockWithDividendRepository)

        //when
        stockViewModel.stockItems.getOrAwaitValue()

        //then
        Assert.assertEquals(1300f, stockViewModel.getDividendMonthly())
        Assert.assertEquals(false, stockViewModel.isVisibleEmptyViewLiveData.getOrAwaitValue())
    }

    private fun getItem() =
        listOf(
            StockWithDividendItem(
                symbol = "BJ",
                stockCnt = 10,
                logoUrl = "choco",
                companyName = "BlackJIn",
                dividends = listOf(
                    DividendItem(
                        ticker = "cho",
                        amount = 120f,
                        frequency = Frequency.MONTHLY
                    )
                )
            )
        )

    private fun getItems() =
        listOf(
            StockWithDividendItem(
                symbol = "BJ",
                stockCnt = 10,
                logoUrl = "banana",
                companyName = "BlackJIn",
                dividends = listOf(
                    DividendItem(
                        ticker = "ban",
                        amount = 120f,
                        frequency = Frequency.MONTHLY
                    )
                )
            ),
            StockWithDividendItem(
                symbol = "BJ",
                stockCnt = 10,
                logoUrl = "apple",
                companyName = "BlackJIn",
                dividends = listOf(
                    DividendItem(
                        ticker = "app",
                        amount = 60f,
                        frequency = Frequency.SEMI
                    )
                )
            )
        )
}