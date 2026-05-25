package com.example.govix.hospital.domain.model

data class HospitalDetail(
    val hospital: Hospital,
    val operationalInfo: List<OperationalInfo>,
)