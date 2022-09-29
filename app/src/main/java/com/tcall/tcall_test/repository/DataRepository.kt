package com.tcall.tcall_test.repository

interface DataRepository<T> {

    suspend fun getData(): T

    suspend fun getTenthChar(): T

    suspend fun getEveryTenthChar(): T

    suspend fun getDistinctCount(): T
}