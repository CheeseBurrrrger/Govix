package com.example.govix.dashboard.domain.model

data class Category(
    val id: Int,
    val name: String,
    val slug: String,
    val icon: String,
    val description: String,
    val colorCode: String,
    val isActive: Boolean,
    val displayOrder: Int
)