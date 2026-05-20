package com.example.govix.profile.data.dto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id")         val id: Int,
    @SerializedName("email")      val email: String,
    @SerializedName("username")   val username: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name")  val lastName: String?,
    @SerializedName("full_name")  val fullName: String?,
    @SerializedName("phone")      val phone: String?,
    @SerializedName("nik")        val nik: String?,
    @SerializedName("region")     val region: String?,
    @SerializedName("address")    val address: String?,
    @SerializedName("gender")     val gender: String?,
    @SerializedName("birth_date") val birthDate: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
)

data class ProfileResponseDto(
    @SerializedName("data") val data: ProfileDto
)