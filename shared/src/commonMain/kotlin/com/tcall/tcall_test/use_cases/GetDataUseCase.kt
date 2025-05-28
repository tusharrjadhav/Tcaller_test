package com.tcall.tcall_test.use_cases

import com.tcall.tcall_test.repository.DataRepository // Points to shared module's version
import com.tcall.tcall_test.util.DataResult // Points to shared module's version
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers // Added for Dispatchers.Default

class GetDataUseCase constructor(
    private val getDataRepository: DataRepository<DataResult<String>>
) {

    fun get10thChar() = flow {
        emit(DataResult.Loading)
        emit(withContext(Dispatchers.Default) { // Changed from ioDispatcher
            getDataRepository.getTenthChar()
        })
    }
    fun getEvery10thChar() = flow {
        emit(DataResult.Loading)
        emit(withContext(Dispatchers.Default) { // Changed from ioDispatcher
            getDataRepository.getEveryTenthChar()
        })
    }
    fun getCharCount() = flow {
        emit(DataResult.Loading)
        emit(withContext(Dispatchers.Default) { // Changed from ioDispatcher
            getDataRepository.getDistinctCount()
        })
    }
}
