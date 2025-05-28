package com.tcall.tcall_test.repository.api

// Assuming Ktor will be used later, which can throw exceptions on non-2xx responses
// and return the body directly. Or, a more generic Result type could be used.
// For now, let's keep it simple. The actual implementation will define how errors are handled.
expect interface NetworkService {
    suspend fun getUrlContent(): String // Changed from Response<String>
}
