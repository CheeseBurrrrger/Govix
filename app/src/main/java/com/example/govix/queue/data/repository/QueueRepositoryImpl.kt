package com.example.govix.queue.data.repository

import com.example.govix.core.data.remote.GovixQueueApi
import com.example.govix.queue.data.mapper.toDomain
import com.example.govix.queue.data.mapper.toDto
import com.example.govix.queue.domain.model.BookQueueRequest
import com.example.govix.queue.domain.model.Queue
import com.example.govix.queue.domain.repository.QueueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QueueRepositoryImpl @Inject constructor(
    private val api: GovixQueueApi
): QueueRepository{
    override suspend fun bookQueue(request: BookQueueRequest): Result<Queue> = withContext(
        Dispatchers.IO){
        runCatching {
            val response = api.bookQueue(request.toDto())
            if(!response.isSuccessful)error("Http ${response.code()}")
            val body = response.body()?:error("Respons kosong")
            if(body.success == false)error(body.message?:"Booking gagal")
            body.data?.toDomain()?:error("Data booking kosong")
        }
    }

    override suspend fun getMyQueues(): Result<List<Queue>> = withContext(
        Dispatchers.IO){
        runCatching {
            val response = api.getMyQueues()
            if(!response.isSuccessful)error("Http ${response.code()}")
            val body = response.body()?:error("Respons kosong")
            if(body.success == false)error(body.message?:"Gagal memuat antrean")
            body.data.orEmpty().map { it.toDomain() }
        }
    }
}