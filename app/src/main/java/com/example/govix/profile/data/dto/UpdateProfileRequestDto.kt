package com.example.govix.profile.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequestDto(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name")  val lastName: String,
    @SerializedName("phone")      val phone: String,
    @SerializedName("nik")        val nik: String,
    @SerializedName("region")     val region: String,
    @SerializedName("address")    val address: String,
    @SerializedName("gender")     val gender: String,
    @SerializedName("birth_date") val birthDate: String,
)