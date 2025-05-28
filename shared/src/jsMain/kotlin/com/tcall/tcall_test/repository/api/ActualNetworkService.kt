package com.tcall.tcall_test.repository.api

import io.ktor.client.*
import io.ktor.client.engine.js.* // JS engine
import io.ktor.client.request.*
import io.ktor.client.statement.*

actual class NetworkService actual constructor() {
    private val client = HttpClient(Js) {
        // Configure Ktor JS client if needed
    }

    private val baseUrl = "https://blog.trello.com/" // Same base URL

    actual suspend fun getUrlContent(): String {
        val response: HttpResponse = client.get(baseUrl + "2018/01/22/life-as-an-android-engineer/")
        if (response.status.value >= 200 && response.status.value < 300) {
            return response.bodyAsText()
        } else {
            throw Exception("HTTP Error: ${response.status.value} ${response.status.description}")
        }
    }
}
