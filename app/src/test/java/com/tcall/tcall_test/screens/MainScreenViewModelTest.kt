package com.tcall.tcall_test.screens

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tcall.tcall_test.use_cases.GetDataUseCase
import com.tcall.tcall_test.util.DataResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockGetDataUseCase: GetDataUseCase

    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setUp() {
        viewModel = MainScreenViewModel(mockGetDataUseCase)
    }

    @Test
    fun `initial state is empty`() = runTest {
        assertThat(viewModel.tenthChar.value).isEqualTo(ScreenState())
        assertThat(viewModel.every10thChar.value).isEqualTo(ScreenState())
        assertThat(viewModel.charCount.value).isEqualTo(ScreenState())
    }

    @Test
    fun `fetchContent success path updates states correctly`() = runTest {
        // Mock successful data results
        whenever(mockGetDataUseCase.get10thChar()).thenReturn(flowOf(DataResult.Loading, DataResult.Success("1")))
        whenever(mockGetDataUseCase.getEvery10thChar()).thenReturn(flowOf(DataResult.Loading, DataResult.Success("1,2,3")))
        whenever(mockGetDataUseCase.getCharCount()).thenReturn(flowOf(DataResult.Loading, DataResult.Success("Count:3")))

        // Test tenthChar state flow
        viewModel.tenthChar.test {
            viewModel.get10thCharacterValue() // Trigger specific part
            if (expectMostRecentItem() == ScreenState()) awaitItem() // Consume initial if it's there
            assertThat(awaitItem()).isEqualTo(ScreenState(loading = true))
            assertThat(awaitItem()).isEqualTo(ScreenState(data = "1"))
            cancelAndConsumeRemainingEvents()
        }

        // Test every10thChar state flow
        viewModel.every10thChar.test {
            viewModel.getEveryTenthCharacter() // Trigger specific part
            if (expectMostRecentItem() == ScreenState()) awaitItem() // Consume initial if it's there
            assertThat(awaitItem()).isEqualTo(ScreenState(loading = true))
            assertThat(awaitItem()).isEqualTo(ScreenState(data = "1,2,3"))
            cancelAndConsumeRemainingEvents()
        }

        // Test charCount state flow
        viewModel.charCount.test {
            viewModel.getCharCount() // Trigger specific part
            if (expectMostRecentItem() == ScreenState()) awaitItem() // Consume initial if it's there
            assertThat(awaitItem()).isEqualTo(ScreenState(loading = true))
            assertThat(awaitItem()).isEqualTo(ScreenState(data = "Count:3"))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchContent success path updates all states after fetchContent call`() = runTest {
        // Mock successful data results
        whenever(mockGetDataUseCase.get10thChar()).thenReturn(flowOf(DataResult.Success("1")))
        whenever(mockGetDataUseCase.getEvery10thChar()).thenReturn(flowOf(DataResult.Success("1,2,3")))
        whenever(mockGetDataUseCase.getCharCount()).thenReturn(flowOf(DataResult.Success("Count:3")))

        viewModel.fetchContent()

        // Assert final states (runTest ensures coroutines launched in viewModelScope complete)
        assertThat(viewModel.tenthChar.value).isEqualTo(ScreenState(data = "1"))
        assertThat(viewModel.every10thChar.value).isEqualTo(ScreenState(data = "1,2,3"))
        assertThat(viewModel.charCount.value).isEqualTo(ScreenState(data = "Count:3"))
    }


    @Test
    fun `fetchContent error path updates states correctly`() = runTest {
        // Mock error results
        val tenthCharException = Exception("Error fetching 10th char")
        val every10thCharException = Exception("Error fetching every 10th char")
        val charCountException = Exception("Error fetching char count")

        whenever(mockGetDataUseCase.get10thChar()).thenReturn(flowOf(DataResult.Error(tenthCharException)))
        whenever(mockGetDataUseCase.getEvery10thChar()).thenReturn(flowOf(DataResult.Loading, DataResult.Error(every10thCharException)))
        whenever(mockGetDataUseCase.getCharCount()).thenReturn(flowOf(DataResult.Loading, DataResult.Error(charCountException)))

        // Test tenthChar error path
        viewModel.tenthChar.test {
            viewModel.get10thCharacterValue()
            if (expectMostRecentItem() == ScreenState()) awaitItem()
            assertThat(awaitItem()).isEqualTo(ScreenState(loading = true))
            assertThat(awaitItem()).isEqualTo(ScreenState(error = tenthCharException.toString()))
            cancelAndConsumeRemainingEvents()
        }
        
        // Test every10thChar error path
        viewModel.every10thChar.test {
            viewModel.getEveryTenthCharacter()
            if (expectMostRecentItem() == ScreenState()) awaitItem()
            assertThat(awaitItem()).isEqualTo(ScreenState(loading = true))
            assertThat(awaitItem()).isEqualTo(ScreenState(error = every10thCharException.toString()))
            cancelAndConsumeRemainingEvents()
        }

        // Test charCount error path
        viewModel.charCount.test {
            viewModel.getCharCount()
            if (expectMostRecentItem() == ScreenState()) awaitItem()
            assertThat(awaitItem()).isEqualTo(ScreenState(loading = true))
            assertThat(awaitItem()).isEqualTo(ScreenState(error = charCountException.toString()))
            cancelAndConsumeRemainingEvents()
        }
    }
    
    // The "loading state is shown for each fetch operation" test is effectively
    // covered by the success and error path tests using Turbine,
    // as they explicitly await the loading state first.
    // This test can be removed or kept if very explicit individual loading state checks are desired
    // without subsequent success/error states in the same Turbine block.
    // For now, I'll remove it as it's largely redundant with the refined success/error tests.
}
