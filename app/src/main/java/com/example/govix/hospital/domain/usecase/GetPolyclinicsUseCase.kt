package com.example.govix.hospital.domain.usecase

import com.example.govix.hospital.domain.repository.HospitalRepository
import com.example.govix.hospital.domain.model.Polyclinic
import javax.inject.Inject

class GetPolyclinicsUseCase @Inject constructor(
    private val repository: HospitalRepository,
){
    suspend operator fun invoke(hospitalId: Int): Result<List<Polyclinic>> =
        repository.getPolyclinics(hospitalId)
}