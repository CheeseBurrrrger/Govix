package com.example.govix.queue.domain.model

data class BookQueueRequest(
    val scheduleId: Int,
    val queueNumber:  Int,
    val scheduleDate: String,
    val patientName: String,
    val patientNik: String,
    val patientBirthdate: String,
)