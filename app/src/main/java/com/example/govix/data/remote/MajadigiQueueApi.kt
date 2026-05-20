package com.example.govix.data.remote

import com.example.govix.data.remote.dto.ApiListResponse
import com.example.govix.data.remote.dto.ApiObjectResponse
import com.example.govix.data.remote.dto.QueueDto
import com.example.govix.data.remote.dto.QueueRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MajadigiQueueApi {
    @POST("api/queues")
    suspend fun bookQueue(@Body body: QueueRequestDto): Response<ApiObjectResponse<QueueDto>>

    @GET("api/queues/my")
    suspend fun getMyQueues(): Response<ApiListResponse<QueueDto>>
}

