package com.example.govix.queue.data.mapper

import com.example.govix.data.remote.dto.QueueDto
import com.example.govix.data.remote.dto.QueueRequestDto
import com.example.govix.queue.domain.model.BookQueueRequest
import com.example.govix.queue.domain.model.Queue

fun QueueDto.toDomain() = Queue(
    id = id?:0,
    scheduleId = scheduleId?:0,
    queueNumber = queueNumber?:0,
    scheduleDate = scheduleDate.orEmpty(),
    patientName = patientName.orEmpty(),
    patientNik = patientNik.orEmpty(),
    patientBirthDate = patientBirthDate.orEmpty(),
    status = status.orEmpty(),
    createdAt = createdAt.orEmpty()
)

fun BookQueueRequest.toDto() = QueueRequestDto(
    scheduleId = scheduleId,
    queueNumber = queueNumber,
    scheduleDate = scheduleDate,
    patientName = patientName,
    patientNik = patientNik,
    patientBirthDate = patientBirthdate
)