package com.example.govix.data.repository

import com.example.govix.data.remote.MajadigiHospitalApi
import com.example.govix.data.remote.dto.ApiListResponse
import com.example.govix.data.remote.dto.ApiObjectResponse
import com.example.govix.data.remote.dto.DoctorDto
import com.example.govix.data.remote.dto.DoctorScheduleDto
import com.example.govix.data.remote.dto.HospitalDto
import com.example.govix.data.remote.dto.OperationalInfoDto
import com.example.govix.data.remote.dto.PolyclinicDto
import com.example.govix.data.remote.dto.RoomAvailabilityPayloadDto
import com.example.govix.data.remote.dto.SimpleErrorBody
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Response

class HospitalRepository(
    private val api: MajadigiHospitalApi,
    private val gson: Gson = Gson(),
) {

    suspend fun getHospitals(city: String? = null): Result<List<HospitalDto>> =
        withContext(Dispatchers.IO) {
            runCatching {
                unwrapList(api.getHospitals(city)).getOrThrow()
            }
        }

    /** Loads all active hospitals (no city filter). */
    suspend fun getAllHospitals(): Result<List<HospitalDto>> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapList(api.getHospitals(null)).getOrThrow() }
        }

    suspend fun getHospital(id: Int): Result<HospitalDto> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapObject(api.getHospital(id)).getOrThrow() }
        }

    suspend fun getPolyclinics(hospitalId: Int): Result<List<PolyclinicDto>> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapList(api.getPolyclinics(hospitalId)).getOrThrow() }
        }

    suspend fun getDoctors(polyclinicId: Int): Result<List<DoctorDto>> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapList(api.getDoctors(polyclinicId)).getOrThrow() }
        }

    suspend fun getSchedules(doctorId: Int): Result<List<DoctorScheduleDto>> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapList(api.getSchedules(doctorId)).getOrThrow() }
        }

    suspend fun getRooms(hospitalId: Int): Result<RoomAvailabilityPayloadDto> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapObject(api.getRooms(hospitalId)).getOrThrow() }
        }

    suspend fun getOperationalInfo(hospitalId: Int): Result<List<OperationalInfoDto>> =
        withContext(Dispatchers.IO) {
            runCatching { unwrapList(api.getOperationalInfo(hospitalId)).getOrThrow() }
        }

    suspend fun loadHospitalDetailBundle(hospitalId: Int): Result<HospitalDetailBundle> =
        withContext(Dispatchers.IO) {
            runCatching {
                coroutineScope {
                    val hospitalDeferred = async { getHospital(hospitalId).getOrThrow() }
                    val infoDeferred = async {
                        getOperationalInfo(hospitalId).getOrElse { emptyList() }
                    }
                    HospitalDetailBundle(
                        hospital = hospitalDeferred.await(),
                        operationalInfo = infoDeferred.await(),
                    )
                }
            }
        }

    private fun <T> unwrapList(response: Response<ApiListResponse<T>>): Result<List<T>> {
        if (!response.isSuccessful) {
            return Result.failure(Exception(errorMessage(response)))
        }
        val body = response.body()
        if (body?.success == false) {
            return Result.failure(Exception(body.message ?: "Permintaan gagal."))
        }
        return Result.success(body?.data.orEmpty())
    }

    private fun <T> unwrapObject(response: Response<ApiObjectResponse<T>>): Result<T> {
        if (!response.isSuccessful) {
            return Result.failure(Exception(errorMessage(response)))
        }
        val body = response.body()
        if (body?.success == false) {
            return Result.failure(Exception(body.message ?: "Permintaan gagal."))
        }
        val data = body?.data
            ?: return Result.failure(Exception("Respons kosong."))
        return Result.success(data)
    }

    private fun errorMessage(response: Response<*>): String {
        val err = response.errorBody()?.string()
        if (!err.isNullOrBlank()) {
            try {
                val parsed = gson.fromJson(err, SimpleErrorBody::class.java)
                val m = parsed.message ?: parsed.error
                if (!m.isNullOrBlank()) return m
            } catch (_: Exception) { /* ignore */ }
            return err
        }
        return "HTTP ${response.code()}"
    }
}

data class HospitalDetailBundle(
    val hospital: HospitalDto,
    val operationalInfo: List<OperationalInfoDto>,
)
