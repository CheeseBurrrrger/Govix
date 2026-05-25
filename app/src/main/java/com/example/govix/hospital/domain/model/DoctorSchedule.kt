package com.example.govix.hospital.domain.model

data class DoctorSchedule(
    val id: Int,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val maxPatients: Int,
    val currentPatients: Int,
    val isFull: Boolean,

)