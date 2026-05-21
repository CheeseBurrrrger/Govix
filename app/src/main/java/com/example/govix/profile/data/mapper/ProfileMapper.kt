package com.example.govix.profile.data.mapper

import com.example.govix.profile.data.dto.ProfileDto
import com.example.govix.profile.data.dto.UpdateProfileRequestDto
import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.domain.model.UpdateProfileRequest

private fun String.toGenderApiValue(): String = when (trim().uppercase()) {
    "LAKI - LAKI", "LAKI-LAKI", "L" -> "L"
    "PEREMPUAN", "P"                 -> "P"
    else                             -> this  // pass through if already correct
}
fun ProfileDto.toDomain(): Profile {
    val apiFullName = fullName.orEmpty()
    val apiFirst = firstName.orEmpty()
    val apiLast = lastName.orEmpty()

    val (derivedFirst, derivedLast) = if (apiFirst.isBlank() && apiLast.isBlank() && apiFullName.isNotBlank()) {
        val parts = apiFullName.trim().split(Regex("\\s+"), limit = 2)
        val first = parts.firstOrNull().orEmpty()
        val last = parts.getOrNull(1).orEmpty()
        first to last
    } else {
        apiFirst to apiLast
    }

    val computedFullName = apiFullName.ifBlank {
        listOf(derivedFirst, derivedLast).filter { it.isNotBlank() }.joinToString(" ")
    }

    return Profile(
        id = id,
        email = email,
        username = username,
        firstName = derivedFirst,
        lastName = derivedLast,
        fullName = computedFullName,
        phone = phone.orEmpty(),
        nik = nik.orEmpty(),
        region = region.orEmpty(),
        address = address.orEmpty(),
        gender = gender.orEmpty(),
        birthDate = birthDate.orEmpty(),
        avatarUrl = avatarUrl,
    )
}

fun UpdateProfileRequest.toDto() = UpdateProfileRequestDto(
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    nik = nik,
    region = region,
    address = address,
    gender = gender.toGenderApiValue(),
    birthDate = birthDate,
)
