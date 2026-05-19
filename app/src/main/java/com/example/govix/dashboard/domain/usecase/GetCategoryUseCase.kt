package com.example.govix.dashboard.domain.usecase

import com.example.govix.dashboard.domain.model.Category
import com.example.govix.dashboard.domain.repository.CategoryRepository

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getCategories()
            .map { list ->
                list.filter { it.isActive }
                    .sortedBy { it.displayOrder }
            }
    }
}