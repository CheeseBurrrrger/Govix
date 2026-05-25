package com.example.govix.hospital.domain.repository

import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.model.HospitalDetail
import com.example.govix.hospital.domain.model.Polyclinic
import com.example.govix.hospital.domain.model.RoomAvailability

interface HospitalRepository{
    suspend fun getHospitals(city: String? = null): Result<List<Hospital>>
    suspend fun getHospitalDetail(id: Int): Result<HospitalDetail>
    suspend fun getPolyclinics(HospitalId: Int): Result<List<Polyclinic>>
    suspend fun getDoctors(polyclinicId: Int): Result<List<Doctor>>
    suspend fun getSchedules(doctorId: Int): Result<List<DoctorSchedule>>
    suspend fun getRooms(hospitalId: Int): Result<RoomAvailability>
}