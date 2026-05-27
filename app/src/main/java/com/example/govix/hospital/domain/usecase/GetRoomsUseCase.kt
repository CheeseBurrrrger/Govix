package com.example.govix.hospital.domain.usecase

import com.example.govix.hospital.domain.model.RoomAvailability
import com.example.govix.hospital.domain.repository.HospitalRepository
import javax.inject.Inject

class GetRoomsUseCase @Inject constructor(
    private val repository: HospitalRepository,
) {
    suspend operator fun invoke(hospitalId: Int): Result<RoomAvailability> =
        repository.getRooms(hospitalId)
}
