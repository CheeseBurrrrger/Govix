package com.example.govix.dashboard.domain.repository

import com.example.govix.dashboard.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}