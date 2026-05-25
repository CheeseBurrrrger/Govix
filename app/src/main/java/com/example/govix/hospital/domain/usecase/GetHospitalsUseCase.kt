package com.example.govix.hospital.domain.usecase

import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.repository.HospitalRepository
import javax.inject.Inject

class GetHospitalsUseCase @Inject constructor(
    private val repository: HospitalRepository,
) {
    suspend operator fun invoke(city: String? = null): Result<List<Hospital>> =
        repository.getHospitals(city)
}