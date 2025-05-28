package com.tcall.tcall_test.use_cases

import com.tcall.tcall_test.repository.DataRepository
import com.tcall.tcall_test.util.DataResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.*

// Fake DataRepository for testing GetDataUseCase
class FakeDataRepository : DataRepository<DataResult<String>> {
    var tenthCharResult: DataResult<String> = DataResult.Loading // Default to loading to catch unhandled
    var everyTenthCharResult: DataResult<String> = DataResult.Loading
    var distinctCountResult: DataResult<String> = DataResult.Loading
    // We don't need getData() for this use case directly, but it's in the interface
    override suspend fun getData(): DataResult<String> = DataResult.Success("Not used in these use cases")

    override suspend fun getTenthChar(): DataResult<String> = tenthCharResult
    override suspend fun getEveryTenthChar(): DataResult<String> = everyTenthCharResult
    override suspend fun getDistinctCount(): DataResult<String> = distinctCountResult
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetDataUseCaseTest {
    private lateinit var fakeDataRepository: FakeDataRepository
    private lateinit var useCase: GetDataUseCase

    @BeforeTest
    fun setup() {
        fakeDataRepository = FakeDataRepository()
        useCase = GetDataUseCase(fakeDataRepository) // Uses Dispatchers.Default internally
    }

    @Test
    fun `get10thChar emits Loading then Success`() = runTest {
        fakeDataRepository.tenthCharResult = DataResult.Success("T")
        val results = mutableListOf<DataResult<String>>()
        val job = launch { useCase.get10thChar().toList(results) }
        job.join() // Wait for the flow to complete

        assertEquals(2, results.size)
        assertTrue(results[0] is DataResult.Loading)
        assertTrue(results[1] is DataResult.Success)
        assertEquals("T", (results[1] as DataResult.Success).data)
    }

    @Test
    fun `get10thChar emits Loading then Error`() = runTest {
        val exception = Exception("Repo Error")
        fakeDataRepository.tenthCharResult = DataResult.Error(exception)
        val results = mutableListOf<DataResult<String>>()
        val job = launch { useCase.get10thChar().toList(results) }
        job.join()

        assertEquals(2, results.size)
        assertTrue(results[0] is DataResult.Loading)
        assertTrue(results[1] is DataResult.Error)
        assertEquals(exception, (results[1] as DataResult.Error).exception)
    }

    // Similar tests for getEvery10thChar() and getCharCount()
    @Test
    fun `getEvery10thChar emits Loading then Success`() = runTest {
        fakeDataRepository.everyTenthCharResult = DataResult.Success("A, B")
        val results = mutableListOf<DataResult<String>>()
        val job = launch { useCase.getEvery10thChar().toList(results) }
        job.join()
        assertEquals(2, results.size)
        assertTrue(results[0] is DataResult.Loading)
        assertTrue(results[1] is DataResult.Success)
        assertEquals("A, B", (results[1] as DataResult.Success).data)
    }

    @Test
    fun `getCharCount emits Loading then Success`() = runTest {
        fakeDataRepository.distinctCountResult = DataResult.Success("Count: 5")
        val results = mutableListOf<DataResult<String>>()
        val job = launch { useCase.getCharCount().toList(results) }
        job.join()
        assertEquals(2, results.size)
        assertTrue(results[0] is DataResult.Loading)
        assertTrue(results[1] is DataResult.Success)
        assertEquals("Count: 5", (results[1] as DataResult.Success).data)
    }
}
