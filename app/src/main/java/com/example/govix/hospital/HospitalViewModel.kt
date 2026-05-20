package com.example.govix.hospital

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.govix.core.data.TokenDataStore
import com.example.govix.core.network.MajadigiRetrofit
import com.example.govix.data.remote.dto.DoctorDto
import com.example.govix.data.remote.dto.DoctorScheduleDto
import com.example.govix.data.remote.dto.HospitalDto
import com.example.govix.data.remote.dto.OperationalInfoDto
import com.example.govix.data.remote.dto.PolyclinicDto
import com.example.govix.data.remote.dto.RoomAvailabilityItemDto
import com.example.govix.data.remote.dto.RoomAvailabilityPayloadDto
import com.example.govix.data.repository.HospitalDetailBundle
import com.example.govix.data.repository.HospitalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HospitalListUiState(
    val isLoading: Boolean = false,
    val hospitals: List<HospitalDto> = emptyList(),
    val error: String? = null,
)

data class HospitalDetailUiState(
    val isLoading: Boolean = false,
    val hospital: HospitalDto? = null,
    val operationalInfo: List<OperationalInfoDto> = emptyList(),
    val error: String? = null,
)

data class HospitalRoomsUiState(
    val isLoading: Boolean = false,
    val summary: RoomSummaryUi = RoomSummaryUi(),
    val rooms: List<RoomAvailabilityItemDto> = emptyList(),
    val updatedAt: String? = null,
    val error: String? = null,
)

data class RoomSummaryUi(
    val total: Int = 0,
    val available: Int = 0,
    val occupied: Int = 0,
)

data class HospitalQueueUiState(
    val isLoadingPolyclinics: Boolean = false,
    val isLoadingDoctors: Boolean = false,
    val isLoadingSchedules: Boolean = false,
    val polyclinics: List<PolyclinicDto> = emptyList(),
    val doctors: List<DoctorDto> = emptyList(),
    val schedules: List<DoctorScheduleDto> = emptyList(),
    val error: String? = null,
)

class HospitalViewModel(
    application: Application,
    private val repository: HospitalRepository,
) : AndroidViewModel(application) {

    private val _listState = MutableStateFlow(HospitalListUiState())
    val listState: StateFlow<HospitalListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(HospitalDetailUiState())
    val detailState: StateFlow<HospitalDetailUiState> = _detailState.asStateFlow()

    private val _roomsState = MutableStateFlow(HospitalRoomsUiState())
    val roomsState: StateFlow<HospitalRoomsUiState> = _roomsState.asStateFlow()

    private val _queueState = MutableStateFlow(HospitalQueueUiState())
    val queueState: StateFlow<HospitalQueueUiState> = _queueState.asStateFlow()

    fun loadHospitals() {
        if (_listState.value.isLoading) return
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true, error = null) }
            repository.getAllHospitals()
                .onSuccess { hospitals ->
                    _listState.update { it.copy(isLoading = false, hospitals = hospitals) }
                }
                .onFailure { e ->
                    _listState.update {
                        it.copy(isLoading = false, error = e.message ?: "Gagal memuat rumah sakit.")
                    }
                }
        }
    }

    fun loadHospitalDetail(hospitalId: Int) {
        viewModelScope.launch {
            _detailState.update { it.copy(isLoading = true, error = null) }
            repository.loadHospitalDetailBundle(hospitalId)
                .onSuccess { bundle: HospitalDetailBundle ->
                    _detailState.update {
                        it.copy(
                            isLoading = false,
                            hospital = bundle.hospital,
                            operationalInfo = bundle.operationalInfo,
                        )
                    }
                }
                .onFailure { e ->
                    _detailState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Gagal memuat detail rumah sakit.",
                        )
                    }
                }
        }
    }

    fun loadRooms(hospitalId: Int) {
        viewModelScope.launch {
            _roomsState.update { it.copy(isLoading = true, error = null) }
            repository.getRooms(hospitalId)
                .onSuccess { payload -> _roomsState.update { mapRooms(payload) } }
                .onFailure { e ->
                    _roomsState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Gagal memuat ketersediaan kamar.",
                        )
                    }
                }
        }
    }

    fun loadPolyclinics(hospitalId: Int) {
        viewModelScope.launch {
            _queueState.update {
                it.copy(
                    isLoadingPolyclinics = true,
                    error = null,
                    polyclinics = emptyList(),
                    doctors = emptyList(),
                    schedules = emptyList(),
                )
            }
            repository.getPolyclinics(hospitalId)
                .onSuccess { list ->
                    _queueState.update { it.copy(isLoadingPolyclinics = false, polyclinics = list) }
                }
                .onFailure { e ->
                    _queueState.update {
                        it.copy(
                            isLoadingPolyclinics = false,
                            error = e.message ?: "Gagal memuat poliklinik.",
                        )
                    }
                }
        }
    }

    fun loadDoctors(polyclinicId: Int) {
        viewModelScope.launch {
            _queueState.update {
                it.copy(isLoadingDoctors = true, doctors = emptyList(), schedules = emptyList())
            }
            repository.getDoctors(polyclinicId)
                .onSuccess { list ->
                    _queueState.update { it.copy(isLoadingDoctors = false, doctors = list) }
                }
                .onFailure { e ->
                    _queueState.update {
                        it.copy(
                            isLoadingDoctors = false,
                            error = e.message ?: "Gagal memuat dokter.",
                        )
                    }
                }
        }
    }

    fun loadSchedules(doctorId: Int) {
        viewModelScope.launch {
            _queueState.update { it.copy(isLoadingSchedules = true, schedules = emptyList()) }
            repository.getSchedules(doctorId)
                .onSuccess { list ->
                    _queueState.update { it.copy(isLoadingSchedules = false, schedules = list) }
                }
                .onFailure { e ->
                    _queueState.update {
                        it.copy(
                            isLoadingSchedules = false,
                            error = e.message ?: "Gagal memuat jadwal.",
                        )
                    }
                }
        }
    }

    fun findHospitalIdByName(name: String): Int? {
        val needle = name.normalizeHospitalKey()
        return _listState.value.hospitals.firstOrNull { hospital ->
            val hay = hospital.name.normalizeHospitalKey()
            hay.contains(needle) || needle.contains(hay.take(12))
        }?.id
    }

    private fun String?.normalizeHospitalKey(): String =
        this.orEmpty().lowercase().replace(".", "").replace(" ", "")

    private fun mapRooms(payload: RoomAvailabilityPayloadDto): HospitalRoomsUiState {
        val details = payload.details.orEmpty()
        val summaryDto = payload.summary
        val total = summaryDto?.total ?: details.sumOf { it.totalBeds ?: 0 }
        val available = summaryDto?.available ?: details.sumOf { it.availableBeds ?: 0 }
        val occupied = (total - available).coerceAtLeast(0)
        val updatedAt = details.firstOrNull()?.updatedAt
        return HospitalRoomsUiState(
            isLoading = false,
            summary = RoomSummaryUi(total = total, available = available, occupied = occupied),
            rooms = details,
            updatedAt = updatedAt,
        )
    }
}

class HospitalViewModelFactory(
    private val application: Application,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val tokenStore = TokenDataStore(application)
        val api = MajadigiRetrofit.hospitalApi(tokenStore)
        val repository = HospitalRepository(api)
        return HospitalViewModel(application, repository) as T
    }
}
