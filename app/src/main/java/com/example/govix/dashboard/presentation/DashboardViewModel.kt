package com.example.govix.features.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.usecase.GetHospitalsUseCase
import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.domain.usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DashboardState {
    object Idle    : DashboardState()
    object Loading : DashboardState()
    data class Success(
        val profile: Profile,
        val nearbyHospitals: List<Hospital>,
    ) : DashboardState()
    data class Error(val message: String?) : DashboardState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getHospitalsUseCase: GetHospitalsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = DashboardState.Loading
            val profileDeferred   = async { getProfileUseCase() }
            val hospitalsDeferred = async {
                getHospitalsUseCase(null)
            }

            val profileResult   = profileDeferred.await()
            val hospitalsResult = hospitalsDeferred.await()

            if (profileResult.isFailure) {
                _state.value = DashboardState.Error(profileResult.exceptionOrNull()?.message)
                return@launch
            }

            val profile = profileResult.getOrThrow()

            // Filter hospitals by user's region (profile.region = city)
            val allHospitals = hospitalsResult.getOrElse { emptyList() }
            val nearby = if (profile.region.isNotBlank()) {
                allHospitals.filter {
                    it.city.contains(profile.region, ignoreCase = true)
                }
            } else {
                allHospitals
            }

            _state.value = DashboardState.Success(
                profile          = profile,
                nearbyHospitals  = nearby,
            )
        }
    }
}