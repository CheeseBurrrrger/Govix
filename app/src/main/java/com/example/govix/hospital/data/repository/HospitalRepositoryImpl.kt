package com.example.govix.hospital.data.repository

import com.example.govix.core.data.remote.GovixHospitalApi
import com.example.govix.hospital.data.mapper.toDomain
import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.model.HospitalDetail
import com.example.govix.hospital.domain.model.Polyclinic
import com.example.govix.hospital.domain.model.RoomAvailability
import com.example.govix.hospital.domain.repository.HospitalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val api: GovixHospitalApi
) : HospitalRepository{
    override suspend fun getHospitals(city: String?): Result<List<Hospital>> = withContext(
        Dispatchers.IO) {
        runCatching {
            val response = api.getHospitals(city)
            if(!response.isSuccessful) error("Http ${response.code()}")
            val body = response.body() ?: error("Respon kosong.")
            if(body.success==false) error(body.message ?: "Gagal memuat rumah sakit.")
            body.data.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun getHospitalDetail(id: Int): Result<HospitalDetail> = withContext(
        Dispatchers.IO){
        runCatching {
            coroutineScope {
                val hospitalDeferred = async {
                    val r= api.getHospital(id)
                    if(!r.isSuccessful)error("Http ${r.code()}")
                    r.body()?.data?.toDomain() ?: error("Data rumah sakit kosong.")
                }
                val infoDeferred = async{
                    val r = api.getOperationalInfo(id)
                    if(!r.isSuccessful)r.body()?.data.orEmpty().map { it.toDomain() }
                    else emptyList()
                }
                HospitalDetail(
                    hospital = hospitalDeferred.await(),
                    operationalInfo = infoDeferred.await()
                )
            }
        }
    }

    override suspend fun getPolyclinics(HospitalId: Int): Result<List<Polyclinic>> = withContext(
        Dispatchers.IO){
        runCatching {
            val response = api.getPolyclinics(HospitalId)
            if (!response.isSuccessful)error("Http ${response.code()}")
            val body = response.body()?: error("Respons kosong.")
            body.data.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun getDoctors(polyclinicId: Int): Result<List<Doctor>> = withContext(
    Dispatchers.IO) {
        runCatching {
            val response = api.getDoctors(polyclinicId)
            if(!response.isSuccessful)error("Http ${response.code()}")
            val body = response.body()?:error("Respons kosong.")
            body.data.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun getSchedules(doctorId: Int): Result<List<DoctorSchedule>> = withContext(
    Dispatchers.IO){
        runCatching {
            val response = api.getSchedules(doctorId)
            if (!response.isSuccessful)error("Http ${response.code()}")
            val body = response.body()?:error("Respons kosong.")
            body.data.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun getRooms(hospitalId: Int): Result<RoomAvailability> = withContext(
    Dispatchers.IO){
        runCatching {
            val response = api.getRooms(hospitalId)
            if(!response.isSuccessful)error("Http ${response.code()}")
            val body = response.body()?:error("Respons kosong.")
            body.data?.toDomain()?:error("Data kamar kosong")
        }
    }
}