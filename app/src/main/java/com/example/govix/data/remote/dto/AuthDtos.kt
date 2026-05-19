package com.example.govix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RegisterRequest(
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val nik: String,
    val address: String,
    /** Prefer ISO `yyyy-MM-dd`; backend may also accept other formats. */
    val birthDate: String,
    /** Majadigi DB uses `L` / `P`. */
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

/** Typical `{ success, message, data: { token } }` wrapper. */
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
