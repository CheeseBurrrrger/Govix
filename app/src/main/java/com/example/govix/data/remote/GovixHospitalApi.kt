package com.example.govix.data.remote

import com.example.govix.data.remote.dto.ApiListResponse
import com.example.govix.data.remote.dto.ApiObjectResponse
import com.example.govix.data.remote.dto.DoctorDto
import com.example.govix.data.remote.dto.DoctorScheduleDto
import com.example.govix.data.remote.dto.HospitalDto
import com.example.govix.data.remote.dto.OperationalInfoDto
import com.example.govix.data.remote.dto.PolyclinicDto
import com.example.govix.data.remote.dto.RoomAvailabilityPayloadDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GovixHospitalApi {
    @GET("api/hospitals")
    suspend fun getHospitals(@Query("city") city: String? = null): Response<ApiListResponse<HospitalDto>>

    @GET("api/hospitals/{id}")
    suspend fun getHospital(@Path("id") id: Int): Response<ApiObjectResponse<HospitalDto>>

    @GET("api/hospitals/{id}/polyclinics")
    suspend fun getPolyclinics(@Path("id") hospitalId: Int): Response<ApiListResponse<PolyclinicDto>>

    @GET("api/hospitals/{id}/rooms")
    suspend fun getRooms(@Path("id") hospitalId: Int): Response<ApiObjectResponse<RoomAvailabilityPayloadDto>>

    @GET("api/hospitals/{id}/info")
    suspend fun getOperationalInfo(
        @Path("id") hospitalId: Int,
        @Query("category") category: String? = null,
    ): Response<ApiListResponse<OperationalInfoDto>>

    @GET("api/polyclinics/{id}/doctors")
    suspend fun getDoctors(@Path("id") polyclinicId: Int): Response<ApiListResponse<DoctorDto>>

    @GET("api/doctors/{id}/schedules")
    suspend fun getSchedules(@Path("id") doctorId: Int): Response<ApiListResponse<DoctorScheduleDto>>
}