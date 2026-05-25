package com.example.govix.hospital.domain.usecase

import com.example.govix.hospital.domain.model.HospitalDetail
import com.example.govix.hospital.domain.repository.HospitalRepository
import javax.inject.Inject

class GetHospitalDetailUseCase @Inject constructor(
    private val repository: HospitalRepository,
){
    suspend operator fun invoke(id:Int): Result<HospitalDetail> =
        repository.getHospitalDetail(id)
}