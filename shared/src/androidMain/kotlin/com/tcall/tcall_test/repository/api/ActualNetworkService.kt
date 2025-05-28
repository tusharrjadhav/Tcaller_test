package com.tcall.tcall_test.repository.api

import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit // Added for builder
import retrofit2.converter.scalars.ScalarsConverterFactory // Added for builder
import retrofit2.http.GET

// This is the Retrofit interface that will be created and used internally
internal interface RetrofitBackedAndroidNetworkService {
    @GET("2018/01/22/life-as-an-android-engineer/")
    suspend fun getUrlContent(): Response<String>
}

// This is the actual implementation of the common expect interface
// It will be responsible for creating and using the Retrofit service.
// This approach encapsulates Retrofit within the androidMain's actual implementation.
actual class NetworkService actual constructor() {

    // In a real app, this base URL would come from a config
    private val baseUrl = "https://blog.trello.com/" // Example base URL

    // Create the Retrofit instance and the service
    // This is a simplified setup. In a real app, Hilt would provide this in the :app module,
    // and this actual class would receive the RetrofitBackedAndroidNetworkService instance.
    // However, to make shared/androidMain self-contained for this actualization:
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: RetrofitBackedAndroidNetworkService = retrofit.create(RetrofitBackedAndroidNetworkService::class.java)

    actual suspend fun getUrlContent(): String {
        try {
            val response = service.getUrlContent()
            if (response.isSuccessful) {
                return response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                throw HttpException(response) // Propagates HTTP errors
            }
        } catch (e: Exception) {
            // Log or handle more gracefully if needed
            throw e // Re-throw for now
        }
    }
}
