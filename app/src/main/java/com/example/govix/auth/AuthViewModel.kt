package com.example.govix.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.govix.core.data.ProfileDraftDataStore
import com.example.govix.core.data.TokenDataStore
import com.example.govix.core.network.MajadigiRetrofit
import com.example.govix.core.util.genderLabelToApi
import com.example.govix.core.util.parseDdMmYyyyToIsoOrNull
import com.example.govix.data.remote.dto.RegisterRequest
import com.example.govix.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiEvent {
    data object NavigateHome : AuthUiEvent
    data class NavigateToLogin(val message: String?) : AuthUiEvent
    data class PlainToast(val message: String) : AuthUiEvent
}

class AuthViewModel(
    application: Application,
    private val repository: AuthRepository,
    private val tokenDataStore: TokenDataStore,
) : AndroidViewModel(application) {

    private val profileDraftDataStore = ProfileDraftDataStore(application)

    private val _hydrated = MutableStateFlow(false)
    val hydrated: StateFlow<Boolean> = _hydrated.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _events = MutableSharedFlow<AuthUiEvent>(extraBufferCapacity = 8)
    val events: SharedFlow<AuthUiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            tokenDataStore.hydrate()
            _hydrated.value = true
        }
    }

    fun hasSession(): Boolean = !tokenDataStore.bearerOrNull().isNullOrBlank()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.login(email, password)
                .onSuccess { _events.emit(AuthUiEvent.NavigateHome) }
                .onFailure { e ->
                    _events.emit(AuthUiEvent.PlainToast(e.message ?: "Gagal masuk."))
                }
            _isLoading.value = false
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        username: String,
        phone: String,
        nik: String,
        region: String,
        address: String,
        birthDateDdMmYyyy: String,
        genderLabel: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            val user = username.trim()
            if (user.isEmpty()) {
                _events.emit(AuthUiEvent.PlainToast("Username wajib diisi."))
                return@launch
            }
            val iso = parseDdMmYyyyToIsoOrNull(birthDateDdMmYyyy)
            if (iso == null) {
                _events.emit(AuthUiEvent.PlainToast("Format tanggal lahir harus DD/MM/YYYY."))
                return@launch
            }
            if (nik.length != 16 || !nik.all { it.isDigit() }) {
                _events.emit(AuthUiEvent.PlainToast("NIK harus 16 digit angka."))
                return@launch
            }
            if (region.isBlank()) {
                _events.emit(AuthUiEvent.PlainToast("Wilayah wajib diisi."))
                return@launch
            }
            val first = firstName.trim()
            val last = lastName.trim()
            val fullName = "$first $last".trim()
            val body = RegisterRequest(
                email = email.trim(),
                username = user,
                password = password,
                firstName = first,
                lastName = last,
                fullName = fullName,
                phone = phone.trim(),
                nik = nik.trim(),
                region = region.trim(),
                address = address.trim(),
                birthDate = iso,
                gender = genderLabelToApi(genderLabel),
            )
            _isLoading.value = true
            repository.register(body)
                .onSuccess { loggedInWithToken ->
                    profileDraftDataStore.saveFromRegister(body)
                    if (loggedInWithToken) {
                        // Backend may return a token on register; app flow expects user to login explicitly.
                        tokenDataStore.clearToken()
                    }
                    _events.emit(AuthUiEvent.NavigateToLogin("Berhasil mendaftar. Silakan masuk."))
                }
                .onFailure { e ->
                    _events.emit(AuthUiEvent.PlainToast(e.message ?: "Pendaftaran gagal."))
                }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.logout()
            _events.emit(AuthUiEvent.NavigateToLogin(null))
            _isLoading.value = false
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.changePassword(oldPassword, newPassword)
                .onSuccess { _events.emit(AuthUiEvent.PlainToast("Kata sandi diperbarui.")) }
                .onFailure { e ->
                    _events.emit(AuthUiEvent.PlainToast(e.message ?: "Gagal mengubah kata sandi."))
                }
            _isLoading.value = false
        }
    }

    fun socialLoginPlaceholder() {
        viewModelScope.launch {
            _events.emit(
                AuthUiEvent.PlainToast("Social login belum dihubungkan ke Google/Facebook SDK."),
            )
        }
    }
}

class AuthViewModelFactory(
    private val application: Application,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val tokenStore = TokenDataStore(application)
        val api = MajadigiRetrofit.authApi(tokenStore)
        val repository = AuthRepository(api, tokenStore)
        return AuthViewModel(application, repository, tokenStore) as T
    }
}
