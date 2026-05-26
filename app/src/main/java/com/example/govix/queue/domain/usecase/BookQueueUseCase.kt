package com.example.govix.queue.domain.usecase

import com.example.govix.core.data.ProfileDraftDataStore
import com.example.govix.queue.domain.model.BookQueueRequest
import com.example.govix.queue.domain.model.Queue
import com.example.govix.queue.domain.repository.QueueRepository
import javax.inject.Inject

class BookQueueUseCase @Inject constructor(
    private val repository: QueueRepository,
    private val profileDraftDataStore: ProfileDraftDataStore
){
    suspend operator fun invoke(request: BookQueueRequest): Result<Queue>{
        val draft = profileDraftDataStore.readDraftOrNull()
        val resolved = request.copy(
            patientName = request.patientName.ifBlank { draft?.fullName.orEmpty() },
            patientNik = request.patientNik.ifBlank { draft?.nik.orEmpty() },
            patientBirthdate = request.patientBirthdate.ifBlank { draft?.birthDate.orEmpty() }
        )
        if(resolved.patientName.isBlank()){
            return Result.failure(Exception("Nama pasien tidak dapat ditemukan, harap lengkapi profil anda terlebih dahulu!"))
        }
        if(resolved.patientNik.length !=16){
            return Result.failure(Exception("NIK pasien tidak valid, harap lengkapi profil anda terlebih dahulu!"))
        }
        if(resolved.patientBirthdate.isBlank()){
            return Result.failure(Exception("Tanggal lahir pasien tidak dapat ditemukan, harap lengkapi profil anda terlebih dahulu!"))
        }
        return repository.bookQueue(resolved)
    }
}