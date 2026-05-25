package com.example.govix.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.domain.model.UpdateProfileRequest
import com.example.govix.profile.domain.usecase.GetProfileUseCase
import com.example.govix.profile.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val updateState: StateFlow<UpdateProfileState> = _updateState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            getProfileUseCase()
                .onSuccess { _profileState.value = ProfileState.Success(it) }
                .onFailure { _profileState.value = ProfileState.Error(it.message) }
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateState.value = UpdateProfileState.Loading
            updateProfileUseCase(request)
                .onSuccess {
                    _updateState.value = UpdateProfileState.Success
                    _profileState.value = ProfileState.Success(it)
                }
                .onFailure { _updateState.value = UpdateProfileState.Error(it.message) }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UpdateProfileState.Idle
    }
}

// States
sealed class ProfileState {
    object Idle    : ProfileState()
    object Loading : ProfileState()
    data class Success(val profile: Profile) : ProfileState()
    data class Error(val message: String?) : ProfileState()
}

sealed class UpdateProfileState {
    object Idle    : UpdateProfileState()
    object Loading : UpdateProfileState()
    object Success : UpdateProfileState()
    data class Error(val message: String?) : UpdateProfileState()
}