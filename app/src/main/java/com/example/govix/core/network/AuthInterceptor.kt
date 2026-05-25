package com.example.govix.core.network

import com.example.govix.core.data.TokenDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenDataStore: TokenDataStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenDataStore.bearerOrNull()
        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}