package com.example.govix.profile.data.repository

import com.example.govix.profile.data.mapper.toDomain
import com.example.govix.profile.data.mapper.toDto
import com.example.govix.profile.data.remote.ProfileApiService
import com.example.govix.profile.domain.model.UpdateProfileRequest
import com.example.govix.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import com.example.govix.profile.domain.model.Profile

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApiService
) : ProfileRepository {

    override suspend fun getProfile(): Result<Profile> = runCatching {
        api.getProfile().data.toDomain()
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> = runCatching {
        api.updateProfile(request.toDto()).data.toDomain()
    }
}