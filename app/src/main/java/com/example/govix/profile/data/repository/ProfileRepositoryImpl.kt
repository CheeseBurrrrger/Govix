package com.example.govix.profile.data.repository

import android.util.Log
import com.example.govix.core.data.ProfileDraftDataStore
import com.example.govix.profile.data.mapper.toDomain
import com.example.govix.profile.data.mapper.toDto
import com.example.govix.profile.data.remote.ProfileApiService
import com.example.govix.profile.domain.model.UpdateProfileRequest
import com.example.govix.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import com.example.govix.profile.domain.model.Profile

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApiService,
    private val draftStore: ProfileDraftDataStore,
) : ProfileRepository {

    override suspend fun getProfile(): Result<Profile> = runCatching {
        val response = api.getProfile()
        Log.d("ProfileDebug", "FULL RESPONSE: $response")
        Log.d("ProfileDebug", "DATA FIELD: ${response.data}")
        val dto = response.data
        Log.d("ProfileDebug", "RAW DTO: $dto")
        val fromApi = dto.toDomain()
        val draft = draftStore.readDraftOrNull()
        val merged = if (draft == null) fromApi else fromApi.mergeMissingFromDraft(draft)
        draftStore.saveFromProfile(merged)
        merged
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> = runCatching {
        val profile = api.updateProfile(request.toDto()).data.toDomain()
        draftStore.saveFromProfile(profile)
        profile
    }
}

private fun Profile.mergeMissingFromDraft(draft: com.example.govix.core.data.ProfileDraft): Profile {
    val mergedFirst = firstName.ifBlank { draft.firstName }
    val mergedLast = lastName.ifBlank { draft.lastName }
    val mergedFull = fullName.ifBlank {
        draft.fullName.ifBlank {
            listOf(mergedFirst, mergedLast).filter { it.isNotBlank() }.joinToString(" ")
        }
    }
    return copy(
        firstName = mergedFirst,
        lastName = mergedLast,
        fullName = mergedFull,
        phone = phone.ifBlank { draft.phone },
        nik = nik.ifBlank { draft.nik },
        region = region.ifBlank { draft.region },
        address = address.ifBlank { draft.address },
        gender = gender.ifBlank { draft.gender },
        birthDate = birthDate.ifBlank { draft.birthDate },
    )
}
