package com.example.govix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QueueRequestDto(
    val scheduleId: Int,
    val queueNumber: String,
    val scheduleDate: String,
    val patientName: String,
    val patientNik: String,
    val patientBirthDate: String,
)

data class QueueDto(
    val id: Int? = null,
    @SerializedName("schedule_id") val scheduleId: Int? = null,
    @SerializedName("queue_number") val queueNumber: String? = null,
    @SerializedName("schedule_date") val scheduleDate: String? = null,
    @SerializedName("patient_name") val patientName: String? = null,
    @SerializedName("patient_nik") val patientNik: String? = null,
    @SerializedName("patient_birth_date") val patientBirthDate: String? = null,
    val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
)

