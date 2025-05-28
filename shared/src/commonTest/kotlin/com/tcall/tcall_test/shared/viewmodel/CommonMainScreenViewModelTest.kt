package com.tcall.tcall_test.shared.viewmodel

import com.tcall.tcall_test.use_cases.GetDataUseCase
import com.tcall.tcall_test.util.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.*

// Fake GetDataUseCase
class FakeGetDataUseCase : GetDataUseCase(FakeDataRepository()) { // Needs a DataRepository, can use a fake one
    var mockTenthCharFlow = flowOf(DataResult.Success("Tenth"))
    var mockEveryTenthCharFlow = flowOf(DataResult.Success("EveryTenth"))
    var mockCharCountFlow = flowOf(DataResult.Success("Count"))

    override fun get10thChar() = mockTenthCharFlow
    override fun getEvery10thChar() = mockEveryTenthCharFlow
    override fun getCharCount() = mockCharCountFlow
}
// Dummy FakeDataRepository for FakeGetDataUseCase constructor
private class FakeDataRepository : com.tcall.tcall_test.repository.DataRepository<DataResult<String>> {
    override suspend fun getData() = DataResult.Success("")
    override suspend fun getTenthChar() = DataResult.Success("")
    override suspend fun getEveryTenthChar() = DataResult.Success("")
    override suspend fun getDistinctCount() = DataResult.Success("")
}


@OptIn(ExperimentalCoroutinesApi::class)
class CommonMainScreenViewModelTest {

    private lateinit var fakeGetDataUseCase: FakeGetDataUseCase
    private lateinit var viewModel: CommonMainScreenViewModel
    private lateinit var testDispatcher: TestDispatcher // StandardTestDispatcher

    @BeforeTest
    fun setup() {
        testDispatcher = StandardTestDispatcher() // Create a StandardTestDispatcher
        Dispatchers.setMain(testDispatcher) // Set main dispatcher for tests

        fakeGetDataUseCase = FakeGetDataUseCase()
        // ViewModel takes a CoroutineScope. For tests, use TestScope with the testDispatcher.
        // TestScope will be created by runTest. Pass its coroutineContext.
        // viewModel = CommonMainScreenViewModel(fakeGetDataUseCase, TestScope(testDispatcher)) // This is one way
        // Or, pass the scope from runTest
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher
    }

    @Test
    fun `initial state is correct`() = runTest(testDispatcher) { // Pass dispatcher to runTest
         viewModel = CommonMainScreenViewModel(fakeGetDataUseCase, this) // Pass TestScope
        val initialState = viewModel.uiState.value
        assertEquals("10th Char: --", initialState.tenthChar)
        assertEquals("Every 10th: --", initialState.everyTenthChar)
        assertEquals("Word Count: --", initialState.wordCount)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `fetchContent updates state to success`() = runTest(testDispatcher) {
         viewModel = CommonMainScreenViewModel(fakeGetDataUseCase, this)
        // Configure fakes to return success
        fakeGetDataUseCase.mockTenthCharFlow = flowOf(DataResult.Loading, DataResult.Success("Test10th"))
        fakeGetDataUseCase.mockEveryTenthCharFlow = flowOf(DataResult.Loading, DataResult.Success("TestEvery10th"))
        fakeGetDataUseCase.mockCharCountFlow = flowOf(DataResult.Loading, DataResult.Success("TestCount"))

        viewModel.fetchContent()
        advanceUntilIdle() // Process all coroutines on the test dispatcher

        val state = viewModel.uiState.value
        assertEquals("10th Char: Test10th", state.tenthChar)
        assertEquals("Every 10th: TestEveryTenth", state.everyTenthChar)
        assertEquals("Word Count: TestCount", state.wordCount)
        assertFalse(state.isLoading) // Assuming isLoading becomes false after all flows complete
        assertNull(state.error)
    }
    
    @Test
    fun `fetchContent handles errors correctly`() = runTest(testDispatcher) {
        viewModel = CommonMainScreenViewModel(fakeGetDataUseCase, this)
        val exception = Exception("Fetch failed")
        // Configure one flow to return an error
        fakeGetDataUseCase.mockTenthCharFlow = flowOf(DataResult.Loading, DataResult.Error(exception))
        fakeGetDataUseCase.mockEveryTenthCharFlow = flowOf(DataResult.Loading, DataResult.Success("TestEvery10th")) // Others succeed
        fakeGetDataUseCase.mockCharCountFlow = flowOf(DataResult.Loading, DataResult.Success("TestCount"))

        viewModel.fetchContent()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        // The error message shown might depend on how errors are aggregated or prioritized in ViewModel
        // Current ViewModel updates state per flow, so last error/success on isLoading wins.
        // The individual text fields will show the error.
        assertEquals("Error: Fetch failed", state.tenthChar)
        assertEquals("Every 10th: TestEveryTenth", state.everyTenthChar)
        assertEquals("Word Count: TestCount", state.wordCount)
        assertFalse(state.isLoading) // Check final loading state logic in ViewModel
    }
}
