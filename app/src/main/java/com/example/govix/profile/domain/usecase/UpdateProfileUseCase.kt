package com.example.govix.profile.domain.usecase

import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.domain.model.UpdateProfileRequest
import com.example.govix.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(request: UpdateProfileRequest): Result<Profile> =
        repository.updateProfile(request)
}