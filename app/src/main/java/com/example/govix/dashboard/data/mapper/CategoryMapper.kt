package com.example.govix.dashboard.data.mapper

import com.example.govix.dashboard.data.dto.CategoryDto
import com.example.govix.dashboard.domain.model.Category

fun CategoryDto.toDomain() = Category(
    id = id,
    name = name,
    slug = slug,
    icon = icon,
    description = description,
    colorCode = colorCode,
    isActive = isActive == 1,   // Int → Boolean
    displayOrder = displayOrder
)