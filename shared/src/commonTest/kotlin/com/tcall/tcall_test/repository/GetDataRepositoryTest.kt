package com.tcall.tcall_test.repository

import com.tcall.tcall_test.repository.api.NetworkService
import com.tcall.tcall_test.util.DataResult
import com.tcall.tcall_test.util.StringOperations // Assuming it's in shared/commonMain/util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest // Uses TestCoroutineScheduler
import kotlin.test.*

// Fake NetworkService for testing GetDataRepository
class FakeNetworkService : NetworkService {
    var contentToReturn: String? = null
    var shouldThrowError = false

    override suspend fun getUrlContent(): String {
        if (shouldThrowError) throw Exception("Network Error")
        return contentToReturn ?: throw IllegalStateException("contentToReturn was not set")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetDataRepositoryTest {

    private lateinit var fakeNetworkService: FakeNetworkService
    private lateinit var repository: GetDataRepository

    @BeforeTest
    fun setup() {
        fakeNetworkService = FakeNetworkService()
        // GetDataRepository now uses Dispatchers.Default internally.
        // For runTest, this is usually fine as it manages dispatchers.
        repository = GetDataRepository(fakeNetworkService)
    }

    @Test
    fun `getData success returns success result`() = runTest {
        fakeNetworkService.contentToReturn = "1234567890abcdef"
        val result = repository.getData()
        assertTrue(result is DataResult.Success)
        assertEquals("1234567890abcdef", result.data)
    }

    @Test
    fun `getData network error returns error result`() = runTest {
        fakeNetworkService.shouldThrowError = true
        val result = repository.getData()
        assertTrue(result is DataResult.Error)
        assertEquals("Network Error", result.exception.message)
    }

    @Test
    fun `getTenthChar success`() = runTest {
        fakeNetworkService.contentToReturn = "123456789T" // T is 10th
        val result = repository.getTenthChar()
        assertTrue(result is DataResult.Success)
        assertEquals(StringOperations.preText10thChar + "T", result.data)
    }

    @Test
    fun `getEveryTenthChar success`() = runTest {
         fakeNetworkService.contentToReturn = "123456789A123456789B123456789C"
         val result = repository.getEveryTenthChar()
         assertTrue(result is DataResult.Success)
         // Expected: "Every 10th is: [A, B, C]"
         assertEquals(StringOperations.preTextEvery10thChar + "[A, B, C]", result.data)
    }

    @Test
    fun `getDistinctCount success`() = runTest {
        fakeNetworkService.contentToReturn = "word1 word2 word1 word3"
        val result = repository.getDistinctCount()
        assertTrue(result is DataResult.Success)
        // Expected: "Distinct word count: 3" (word1, word2, word3)
        assertEquals(StringOperations.preTextDistinct + "3", result.data)
    }
}
