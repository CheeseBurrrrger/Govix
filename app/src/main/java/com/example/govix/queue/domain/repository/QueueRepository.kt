package com.example.govix.queue.domain.repository

import com.example.govix.queue.domain.model.BookQueueRequest
import com.example.govix.queue.domain.model.Queue

interface QueueRepository{
    suspend fun bookQueue(request: BookQueueRequest): Result<Queue>
    suspend fun getMyQueues(): Result<List<Queue>>
}