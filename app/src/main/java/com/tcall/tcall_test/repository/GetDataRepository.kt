package com.tcall.tcall_test.repository

import com.tcall.tcall_test.repository.api.NetworkService
import com.tcall.tcall_test.util.DataResult
import com.tcall.tcall_test.util.StringOperations
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class GetDataRepository @Inject constructor(
    private val networkService: NetworkService,
    private val ioDispatcher: CoroutineDispatcher
) : DataRepository<DataResult<String>> {

    override suspend fun getData(): DataResult<String> = try {
        withContext(ioDispatcher) {
            val content = networkService.getUrlContent()
            content.body()
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