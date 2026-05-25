package com.example.govix.hospital.domain.usecase

import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.repository.HospitalRepository
import javax.inject.Inject

class GetSchedulesUseCase @Inject constructor(
    private val repository: HospitalRepository
){
    suspend operator fun invoke(doctorId: Int): Result<List<DoctorSchedule>> =
        repository.getSchedules(doctorId)
}