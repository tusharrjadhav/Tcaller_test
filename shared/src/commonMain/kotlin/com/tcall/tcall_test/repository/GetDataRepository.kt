package com.tcall.tcall_test.repository

import com.tcall.tcall_test.repository.api.NetworkService // expect interface
import com.tcall.tcall_test.util.DataResult
import com.tcall.tcall_test.util.StringOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Removed Hilt annotations: @Singleton, @Inject
// Removed ioDispatcher from constructor
class GetDataRepository constructor(
    private val networkService: NetworkService
) : DataRepository<DataResult<String>> { // DataRepository from commonMain

    override suspend fun getData(): DataResult<String> = try {
        // Changed from withContext(ioDispatcher) to withContext(Dispatchers.Default)
        withContext(Dispatchers.Default) {
            networkService.getUrlContent() // Directly returns String
        }.let {
           DataResult.Success(it)
        }
    } catch (e: Exception) {
        DataResult.Error(e)
    }

    override suspend fun getTenthChar(): DataResult<String> =
        when (val data = getData()) {
            is DataResult.Success -> {
                DataResult.Success(
                    StringOperations
                        .getTenthCharacter(
                            data.data
                        )
                )
            }
            else -> {
                data
            }
        }

    override suspend fun getEveryTenthChar(): DataResult<String> =
        when (val data = getData()) {
            is DataResult.Success -> {
                DataResult.Success(
                    StringOperations.getEveryTenthCharacter(
                            data.data
                        )
                )
            }
            else -> {
                data
            }
        }

    override suspend fun getDistinctCount(): DataResult<String> =
        when (val data = getData()) {
            is DataResult.Success -> {
                DataResult.Success(
                    StringOperations.getDistinctWordCount(
                            data.data
                        )
                )
            }
            else -> {
                data
            }
        }
}
