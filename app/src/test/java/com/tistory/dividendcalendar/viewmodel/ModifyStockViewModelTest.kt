package com.tistory.dividendcalendar.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.tistory.dividendcalendar.CoroutineTestRule
import com.tistory.dividendcalendar.getOrAwaitValue
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockDialogFragment
import com.tistory.dividendcalendar.presentation.dialog.ModifyStockViewModel
import com.tistory.domain.repository.ModifyStockCountUsecase
import com.tistory.domain.usecase.AddStockUsecase
import com.tistory.domain.usecase.DeleteStockUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ModifyStockViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var modifyStockViewModel: ModifyStockViewModel

    private val addStockUsecase = Mockito.mock(AddStockUsecase::class.java)
    private val deleteStockUsecase = Mockito.mock(DeleteStockUsecase::class.java)
    private val modifyStockCountUsecase = Mockito.mock(ModifyStockCountUsecase::class.java)

    private var context = Mockito.mock(Context::class.java)

    @Before
    fun setUp() {
        //..
    }

    @Test
    fun `ADD 타입 다이얼로그 상태 확인`() {
        //given
        val savedStateHandle = SavedStateHandle(
            mapOf(
                Pair(
                    ModifyStockDialogFragment.ARGUMENT_TYPE,
                    ModifyStockDialogFragment.DialogType.ADD
                )
            )
        )
        modifyStockViewModel = ModifyStockViewModel(
            context = context,
            addStockUsecase = addStockUsecase,
            deleteStockUsecase = deleteStockUsecase,
            modifyStockCountUsecase = modifyStockCountUsecase,
            savedStateHandle = savedStateHandle
        )

        //when
        //init

        //then
        val deleteVisible = modifyStockViewModel.deleteVisible.getOrAwaitValue()
        val tickerEnable = modifyStockViewModel.editTickerEnable.getOrAwaitValue()

        Assert.assertEquals(false, deleteVisible)
        Assert.assertEquals(true, tickerEnable)
    }

    @Test
    fun `MODIFY 타입 다이얼로그 상태 확인`() {
        //given
        val savedStateHandle = SavedStateHandle(
            mapOf(
                Pair(
                    ModifyStockDialogFragment.ARGUMENT_TYPE,
                    ModifyStockDialogFragment.DialogType.MODIFY
                )
            )
        )
        modifyStockViewModel = ModifyStockViewModel(
            context = context,
            addStockUsecase = addStockUsecase,
            deleteStockUsecase = deleteStockUsecase,
            modifyStockCountUsecase = modifyStockCountUsecase,
            savedStateHandle = savedStateHandle
        )

        //when
        //init

        //then
        val deleteVisible = modifyStockViewModel.deleteVisible.getOrAwaitValue()
        val tickerEnable = modifyStockViewModel.editTickerEnable.getOrAwaitValue()

        Assert.assertEquals(true, deleteVisible)
        Assert.assertEquals(false, tickerEnable)
    }
}