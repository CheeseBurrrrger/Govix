package com.example.govix.profile.domain.model

data class Profile(
    val id: Int,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val phone: String,
    val nik: String,
    val region: String,
    val address: String,
    val gender: String,
    val birthDate: String,
    val avatarUrl: String?,
)