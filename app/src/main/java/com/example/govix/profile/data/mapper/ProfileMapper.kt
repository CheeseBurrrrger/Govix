package com.example.govix.profile.data.mapper

import com.example.govix.profile.data.dto.ProfileDto
import com.example.govix.profile.data.dto.UpdateProfileRequestDto
import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.domain.model.UpdateProfileRequest

fun ProfileDto.toDomain() = Profile(
    id = id,
    email = email,
    username = username,
    firstName = firstName.orEmpty(),
    lastName = lastName.orEmpty(),
    fullName = fullName.orEmpty(),
    phone = phone.orEmpty(),
    nik = nik.orEmpty(),
    region = region.orEmpty(),
    address = address.orEmpty(),
    gender = gender.orEmpty(),
    birthDate = birthDate.orEmpty(),
    avatarUrl = avatarUrl,
)

fun UpdateProfileRequest.toDto() = UpdateProfileRequestDto(
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    nik = nik,
    region = region,
    address = address,
    gender = gender,
    birthDate = birthDate,
)