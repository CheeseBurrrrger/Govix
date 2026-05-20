package com.example.govix.data.repository

import com.example.govix.data.remote.MajadigiQueueApi
import com.example.govix.data.remote.dto.QueueDto
import com.example.govix.data.remote.dto.QueueRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class QueueRepository(
    private val api: MajadigiQueueApi,
) {

    suspend fun bookQueue(body: QueueRequestDto): Result<QueueDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response = api.bookQueue(body)
            ensureOk(response).getOrThrow()
            val payload = response.body() ?: error("Respons kosong.")
            if (payload.success == false) error(payload.message ?: "Booking gagal.")
            payload.data ?: error(payload.message ?: "Booking gagal.")
        }
    }

    suspend fun getMyQueues(): Result<List<QueueDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = api.getMyQueues()
            ensureOk(response).getOrThrow()
            val payload = response.body() ?: error("Respons kosong.")
            if (payload.success == false) error(payload.message ?: "Gagal memuat antrean.")
            payload.data.orEmpty()
        }
    }

    private fun ensureOk(response: Response<*>): Result<Unit> {
        if (response.isSuccessful) return Result.success(Unit)
        val err = response.errorBody()?.string()
        return Result.failure(Exception(err?.takeIf { it.isNotBlank() } ?: "HTTP ${response.code()}"))
    }
}
