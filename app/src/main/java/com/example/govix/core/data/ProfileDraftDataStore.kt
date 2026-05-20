package com.example.govix.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.govix.data.remote.dto.RegisterRequest
import com.example.govix.profile.domain.model.Profile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.profileDraftDataStore: DataStore<Preferences> by preferencesDataStore(name = "govix_profile_draft")

data class ProfileDraft(
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val phone: String,
    val nik: String,
    val region: String,
    val address: String,
    val gender: String,
    val birthDate: String,
)

class ProfileDraftDataStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val dataStore = context.applicationContext.profileDraftDataStore

    private val firstNameKey = stringPreferencesKey("first_name")
    private val lastNameKey = stringPreferencesKey("last_name")
    private val fullNameKey = stringPreferencesKey("full_name")
    private val phoneKey = stringPreferencesKey("phone")
    private val nikKey = stringPreferencesKey("nik")
    private val regionKey = stringPreferencesKey("region")
    private val addressKey = stringPreferencesKey("address")
    private val genderKey = stringPreferencesKey("gender")
    private val birthDateKey = stringPreferencesKey("birth_date")

    suspend fun readDraftOrNull(): ProfileDraft? {
        val prefs = dataStore.data.first()
        val any =
            prefs[firstNameKey] != null ||
                prefs[lastNameKey] != null ||
                prefs[fullNameKey] != null ||
                prefs[phoneKey] != null ||
                prefs[nikKey] != null ||
                prefs[regionKey] != null ||
                prefs[addressKey] != null ||
                prefs[genderKey] != null ||
                prefs[birthDateKey] != null
        if (!any) return null

        return ProfileDraft(
            firstName = prefs[firstNameKey].orEmpty(),
            lastName = prefs[lastNameKey].orEmpty(),
            fullName = prefs[fullNameKey].orEmpty(),
            phone = prefs[phoneKey].orEmpty(),
            nik = prefs[nikKey].orEmpty(),
            region = prefs[regionKey].orEmpty(),
            address = prefs[addressKey].orEmpty(),
            gender = prefs[genderKey].orEmpty(),
            birthDate = prefs[birthDateKey].orEmpty(),
        )
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(firstNameKey)
            prefs.remove(lastNameKey)
            prefs.remove(fullNameKey)
            prefs.remove(phoneKey)
            prefs.remove(nikKey)
            prefs.remove(regionKey)
            prefs.remove(addressKey)
            prefs.remove(genderKey)
            prefs.remove(birthDateKey)
        }
    }

    suspend fun saveFromRegister(request: RegisterRequest) {
        upsertDraft(
            ProfileDraft(
                firstName = request.firstName,
                lastName = request.lastName,
                fullName = request.fullName,
                phone = request.phone,
                nik = request.nik,
                region = request.region,
                address = request.address,
                gender = request.gender,
                birthDate = request.birthDate,
            )
        )
    }

    suspend fun saveFromProfile(profile: Profile) {
        val computedFullName = profile.fullName.ifBlank {
            listOf(profile.firstName, profile.lastName).filter { it.isNotBlank() }.joinToString(" ")
        }
        upsertDraft(
            ProfileDraft(
                firstName = profile.firstName,
                lastName = profile.lastName,
                fullName = computedFullName,
                phone = profile.phone,
                nik = profile.nik,
                region = profile.region,
                address = profile.address,
                gender = profile.gender,
                birthDate = profile.birthDate,
            )
        )
    }

    private suspend fun upsertDraft(incoming: ProfileDraft) {
        val existing = readDraftOrNull()
        val merged = if (existing == null) {
            incoming
        } else {
            ProfileDraft(
                firstName = incoming.firstName.ifBlank { existing.firstName },
                lastName = incoming.lastName.ifBlank { existing.lastName },
                fullName = incoming.fullName.ifBlank { existing.fullName },
                phone = incoming.phone.ifBlank { existing.phone },
                nik = incoming.nik.ifBlank { existing.nik },
                region = incoming.region.ifBlank { existing.region },
                address = incoming.address.ifBlank { existing.address },
                gender = incoming.gender.ifBlank { existing.gender },
                birthDate = incoming.birthDate.ifBlank { existing.birthDate },
            )
        }
        dataStore.edit { prefs ->
            prefs[firstNameKey] = merged.firstName
            prefs[lastNameKey] = merged.lastName
            prefs[fullNameKey] = merged.fullName
            prefs[phoneKey] = merged.phone
            prefs[nikKey] = merged.nik
            prefs[regionKey] = merged.region
            prefs[addressKey] = merged.address
            prefs[genderKey] = merged.gender
            prefs[birthDateKey] = merged.birthDate
        }
    }
}
