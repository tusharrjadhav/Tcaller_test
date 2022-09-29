package com.tcall.tcall_test.use_cases

import com.tcall.tcall_test.di.IoDispatcher
import com.tcall.tcall_test.repository.DataRepository
import com.tcall.tcall_test.repository.GetDataRepository
import com.tcall.tcall_test.util.DataResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class GetDataUseCase @Inject constructor(
    private val getDataRepository: DataRepository<DataResult<String>>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher) {

    fun get10thChar() = flow {
        emit(DataResult.Loading)
        emit(withContext(ioDispatcher) {
            getDataRepository.getTenthChar()
        })
    }
    fun getEvery10thChar() = flow {
        emit(DataResult.Loading)
        emit(withContext(ioDispatcher) {
            getDataRepository.getEveryTenthChar()
        })
    }
    fun getCharCount() = flow {
        emit(DataResult.Loading)
        emit(withContext(ioDispatcher) {
            getDataRepository.getDistinctCount()
        })
    }
}