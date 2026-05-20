package com.example.govix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiListResponse<T>(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<T>? = null,
)

data class ApiObjectResponse<T>(
    val success: Boolean? = null,
    val message: String? = null,
    val data: T? = null,
)

data class HospitalDto(
    val id: Int? = null,
    val name: String? = null,
    @SerializedName("short_name") val shortName: String? = null,
    val city: String? = null,
    val address: String? = null,
    val description: String? = null,
    val website: String? = null,
    val phone: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
)

data class PolyclinicDto(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    @SerializedName("floor_location") val floorLocation: String? = null,
)

data class DoctorDto(
    val id: Int? = null,
    val name: String? = null,
    val title: String? = null,
    val specialization: String? = null,
)

data class DoctorScheduleDto(
    val id: Int? = null,
    @SerializedName("day_of_week") val dayOfWeek: String? = null,
    @SerializedName("start_time") val startTime: String? = null,
    @SerializedName("end_time") val endTime: String? = null,
    @SerializedName("max_patients") val maxPatients: Int? = null,
    @SerializedName("current_patients") val currentPatients: Int? = null,
)

data class RoomAvailabilitySummaryDto(
    val total: Int? = null,
    val available: Int? = null,
)

data class RoomAvailabilityItemDto(
    @SerializedName("room_name") val roomName: String? = null,
    @SerializedName("room_class") val roomClass: String? = null,
    @SerializedName("total_beds") val totalBeds: Int? = null,
    @SerializedName("available_beds") val availableBeds: Int? = null,
    @SerializedName("occupied_beds") val occupiedBeds: Int? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
)

data class RoomAvailabilityPayloadDto(
    val summary: RoomAvailabilitySummaryDto? = null,
    val details: List<RoomAvailabilityItemDto>? = null,
)

data class OperationalInfoDto(
    val category: String? = null,
    val content: String? = null,
    @SerializedName("display_order") val displayOrder: Int? = null,
)
