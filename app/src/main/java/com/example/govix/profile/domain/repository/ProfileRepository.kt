package com.example.govix.profile.domain.repository

import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.domain.model.UpdateProfileRequest

interface ProfileRepository {
    suspend fun getProfile(): Result<Profile>
    suspend fun updateProfile(request: UpdateProfileRequest): kotlin.Result<Profile>
}