package com.tcall.tcall_test.repository.api

import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("2018/01/22/life-as-an-android-engineer/")
    suspend fun getUrlContent(): Response<String>
}