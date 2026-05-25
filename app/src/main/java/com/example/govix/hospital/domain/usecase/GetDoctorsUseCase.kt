package com.example.govix.hospital.domain.usecase

import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.repository.HospitalRepository
import javax.inject.Inject

class GetDoctorsUseCase @Inject constructor(
    private val repository: HospitalRepository
){
    suspend operator fun invoke(polyclinicId: Int): Result<List<Doctor>> =
        repository.getDoctors(polyclinicId)
}