package com.example.govix.hospital.domain.model

data class Hospital(
    val id: Int,
    val name: String,
    val shortName: String,
    val city: String,
    val address: String,
    val description: String,
    val website: String,
    val phone: String,
    val imageUrl: String?,
)