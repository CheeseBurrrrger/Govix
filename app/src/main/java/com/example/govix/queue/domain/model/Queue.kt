package com.example.govix.queue.domain.model

data class Queue(
    val id: Int,
    val scheduleId: Int,
    val queueNumber: Int,
    val scheduleDate: String,
    val patientName: String,
    val patientNik: String,
    val patientBirthDate: String,
    val status: String,
    val createdAt: String,
)