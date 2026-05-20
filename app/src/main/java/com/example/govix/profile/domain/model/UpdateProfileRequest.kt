package com.example.govix.profile.domain.model

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val nik: String,
    val region: String,
    val address: String,
    val gender: String,
    val birthDate: String,
)