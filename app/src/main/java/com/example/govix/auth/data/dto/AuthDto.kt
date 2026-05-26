package com.example.govix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("full_name") val fullName: String,
    val phone: String,
    val nik: String,
    val region: String,
    val address: String,
    @SerializedName("birth_date") val birthDate: String,
    val gender: String,
)

data class ChangePasswordRequest(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String,
)

data class SocialLoginRequest(
    val provider: String,
    @SerializedName("idToken") val idToken: String,
)

data class AuthWrappedResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val data: AuthDataPayload? = null,
)

data class AuthDataPayload(
    val token: String? = null,
)

data class SimpleErrorBody(
    val message: String? = null,
    val error: String? = null,
)
