package com.example.govix.hospital.domain.model

data class RoomSummary(
    val total: Int,
    val available: Int,
)

data class Room(
    val roomName: String,
    val roomClass: String,
    val totalBeds: Int,
    val availableBeds: Int,
    val occupiedBeds: Int,
    val updatedAt: String,
)

data class RoomAvailability(
    val summary: RoomSummary,
    val rooms: List<Room>
)