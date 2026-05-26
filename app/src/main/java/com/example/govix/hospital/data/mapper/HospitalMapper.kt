package com.example.govix.hospital.data.mapper

import com.example.govix.data.remote.dto.DoctorDto
import com.example.govix.data.remote.dto.DoctorScheduleDto
import com.example.govix.data.remote.dto.HospitalDto
import com.example.govix.data.remote.dto.OperationalInfoDto
import com.example.govix.data.remote.dto.PolyclinicDto
import com.example.govix.data.remote.dto.RoomAvailabilityItemDto
import com.example.govix.data.remote.dto.RoomAvailabilityPayloadDto
import com.example.govix.data.remote.dto.RoomAvailabilitySummaryDto
import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.model.HospitalDetail
import com.example.govix.hospital.domain.model.OperationalInfo
import com.example.govix.hospital.domain.model.Polyclinic
import com.example.govix.hospital.domain.model.Room
import com.example.govix.hospital.domain.model.RoomAvailability
import com.example.govix.hospital.domain.model.RoomSummary


data class HospitalDetailBundle(
    val hospital: HospitalDto,
    val operationalInfo: List<OperationalInfoDto>,
)
fun HospitalDto.toDomain() = Hospital(
    id = id ?: 0,
    name = name.orEmpty(),
    shortName = shortName.orEmpty(),
    city = city.orEmpty(),
    address = address.orEmpty(),
    description = description.orEmpty(),
    website = website.orEmpty(),
    phone = phone.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
)

fun PolyclinicDto.toDomain() = Polyclinic(
    id = id ?: 0,
    name = name.orEmpty(),
    description = description.orEmpty(),
    floorLocation = floorLocation.orEmpty()
)

fun DoctorDto.toDomain() = Doctor(
    id = id ?: 0,
    name = name.orEmpty(),
    title = title.orEmpty(),
    specialization = specialization.orEmpty(),
)

fun DoctorScheduleDto.toDomain() = DoctorSchedule(
    id = id?:0,
    dayOfWeek = dayOfWeek.orEmpty(),
    startTime = startTime.orEmpty(),
    endTime = endTime.orEmpty(),
    maxPatients = maxPatients ?: 0,
    currentPatients = currentPatients ?: 0,
    isFull = (currentPatients ?: 0) >= (maxPatients ?: 0),
)

fun RoomAvailabilitySummaryDto.toDomain() = RoomSummary(
    total = total ?: 0,
    available = available ?: 0
)

fun RoomAvailabilityItemDto.toDomain()= Room(
    roomName = roomName.orEmpty(),
    roomClass = roomClass.orEmpty(),
    totalBeds = totalBeds?:0,
    availableBeds = availableBeds?:0,
    occupiedBeds = occupiedBeds?:0,
    updatedAt = updatedAt.orEmpty()
)

fun  RoomAvailabilityPayloadDto.toDomain() = RoomAvailability(
    summary = summary?.toDomain()?: RoomSummary(0,0),
    rooms = details.orEmpty().map { it.toDomain() }
)

fun OperationalInfoDto.toDomain() = OperationalInfo(
    category = category.orEmpty(),
    content = content.orEmpty(),
    displayOrder = displayOrder?:0
)

fun HospitalDetailBundle.toDomain() = HospitalDetail(
    hospital = hospital.toDomain(),
    operationalInfo = operationalInfo.map { it.toDomain() }
)