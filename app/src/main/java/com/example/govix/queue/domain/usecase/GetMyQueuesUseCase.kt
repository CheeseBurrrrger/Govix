package com.example.govix.queue.domain.usecase

import com.example.govix.queue.domain.model.Queue
import com.example.govix.queue.domain.repository.QueueRepository
import javax.inject.Inject

class GetMyQueuesUseCase @Inject constructor(
    private val repository: QueueRepository,
){
    suspend operator fun invoke(): Result<List<Queue>> = repository.getMyQueues()
}