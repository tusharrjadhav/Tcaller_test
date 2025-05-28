package com.tcall.tcall_test.repository.api

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

actual class NetworkService actual constructor() {
    private val client = HttpClient(Darwin) {
        // Configure Ktor client if needed (e.g., logging, default headers)
        // engine {
        //    configureRequest {
        //        setAllowsCellularAccess(true)
        //    }
        // }
    }

    // Base URL needs to be defined, similar to how it was in Android's NetworkModule
    private val baseUrl = "https://blog.trello.com/" 

    actual suspend fun getUrlContent(): String {
        val response: HttpResponse = client.get(baseUrl + "2018/01/22/life-as-an-android-engineer/")
        // Ensure to check response.status before reading body if not relying on exceptions for errors
        if (response.status.value >= 200 && response.status.value < 300) {
            return response.bodyAsText()
        } else {
            // Consider a more specific exception type if available/appropriate
            throw Exception("HTTP Error: ${response.status.value} ${response.status.description}")
        }
    }
}
