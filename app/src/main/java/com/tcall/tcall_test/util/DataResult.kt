package com.tcall.tcall_test.util

sealed class DataResult<out R> {

    data class Success<out T>(val data: T?) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}

val DataResult<*>.succeeded
    get() = this is DataResult.Success && data != null