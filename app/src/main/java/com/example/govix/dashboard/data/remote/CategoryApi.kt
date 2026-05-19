package com.example.govix.dashboard.data.remote

interface CategoryApi {
    @GET("api/categories")
    suspend fun getCategories(): CategoryResponse
}