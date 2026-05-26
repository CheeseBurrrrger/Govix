package com.example.govix.hospital.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.model.OperationalInfo
import com.example.govix.hospital.domain.model.Polyclinic
import com.example.govix.hospital.domain.model.Room
import com.example.govix.hospital.domain.model.RoomSummary
import com.example.govix.hospital.domain.usecase.GetDoctorsUseCase
import com.example.govix.hospital.domain.usecase.GetHospitalDetailUseCase
import com.example.govix.hospital.domain.usecase.GetHospitalsUseCase
import com.example.govix.hospital.domain.usecase.GetPolyclinicsUseCase
import com.example.govix.hospital.domain.usecase.GetSchedulesUseCase
import com.example.govix.queue.domain.model.BookQueueRequest
import com.example.govix.queue.domain.usecase.BookQueueUseCase
import com.example.govix.queue.domain.usecase.GetMyQueuesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HospitalListUiState(
    val hospitals: List<Hospital> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class HospitalDetailUiState(
    val hospital: Hospital? = null,
    val operationalInfo: List<OperationalInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class HospitalQueueUiState(
    val polyclinics: List<Polyclinic> = emptyList(),
    val doctors: List<Doctor> = emptyList(),
    val schedules: List<DoctorSchedule> = emptyList(),
    val isLoadingPolyclinics: Boolean = false,
    val isLoadingDoctors: Boolean = false,
    val isLoadingSchedules: Boolean = false,
    val error: String? = null,
)

data class HospitalRoomsUiState(
    val summary: RoomSummary = RoomSummary(total = 0, available = 0),
    val rooms: List<Room> = emptyList(),
    val updatedAt: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class MyQueuesUiState(
    val queues: List<BookedQueue> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class BookedQueue(
    val id: Int,
    val queueNumber: Int,
    val scheduleDate: String,
    val patientName: String,
    val patientNik: String,
    val status: String,
)

sealed class QueueBookingState {
    object Idle : QueueBookingState()
    object Loading : QueueBookingState()
    data class Success(val queueNumber: Int) : QueueBookingState()
    data class Error(val message: String?) : QueueBookingState()
}


@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val getHospitalsUseCase: GetHospitalsUseCase,
    private val getHospitalDetailUseCase: GetHospitalDetailUseCase,
    private val getPolyclinicsUseCase: GetPolyclinicsUseCase,
    private val getDoctorsUseCase: GetDoctorsUseCase,
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val bookQueueUseCase: BookQueueUseCase,
    private val getMyQueuesUseCase: GetMyQueuesUseCase,
) : ViewModel() {

    private val _listState = MutableStateFlow(HospitalListUiState())
    val listState: StateFlow<HospitalListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(HospitalDetailUiState())
    val detailState: StateFlow<HospitalDetailUiState> = _detailState.asStateFlow()

    private val _queueState = MutableStateFlow(HospitalQueueUiState())
    val queueState: StateFlow<HospitalQueueUiState> = _queueState.asStateFlow()

    private val _roomsState = MutableStateFlow(HospitalRoomsUiState())
    val roomsState: StateFlow<HospitalRoomsUiState> = _roomsState.asStateFlow()

    private val _myQueuesState = MutableStateFlow(MyQueuesUiState())
    val myQueuesState: StateFlow<MyQueuesUiState> = _myQueuesState.asStateFlow()

    private val _bookingState = MutableStateFlow<QueueBookingState>(QueueBookingState.Idle)
    val bookingState: StateFlow<QueueBookingState> = _bookingState.asStateFlow()

    fun loadHospitals(city: String? = null) {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true, error = null)
            getHospitalsUseCase(city)
                .onSuccess { _listState.value = HospitalListUiState(hospitals = it) }
                .onFailure { _listState.value = HospitalListUiState(isLoading = false, error = it.message) }
        }
    }

    fun loadHospitalDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(isLoading = true, error = null)
            getHospitalDetailUseCase(id)
                .onSuccess {
                    _detailState.value = HospitalDetailUiState(
                        hospital = it.hospital,
                        operationalInfo = it.operationalInfo,
                    )
                }
                .onFailure {
                    _detailState.value = _detailState.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun loadPolyclinics(hospitalId: Int) {
        viewModelScope.launch {
            _queueState.value = _queueState.value.copy(
                isLoadingPolyclinics = true,
                error = null,
                doctors = emptyList(),
                schedules = emptyList(),
            )
            getPolyclinicsUseCase(hospitalId)
                .onSuccess {
                    _queueState.value = _queueState.value.copy(
                        polyclinics = it,
                        isLoadingPolyclinics = false,
                    )
                }
                .onFailure {
                    _queueState.value = _queueState.value.copy(
                        isLoadingPolyclinics = false,
                        error = it.message,
                    )
                }
        }
    }

    fun loadDoctors(polyclinicId: Int) {
        viewModelScope.launch {
            _queueState.value = _queueState.value.copy(
                isLoadingDoctors = true,
                error = null,
                schedules = emptyList(),
            )
            getDoctorsUseCase(polyclinicId)
                .onSuccess {
                    _queueState.value = _queueState.value.copy(
                        doctors = it,
                        isLoadingDoctors = false,
                    )
                }
                .onFailure {
                    _queueState.value = _queueState.value.copy(
                        isLoadingDoctors = false,
                        error = it.message,
                    )
                }
        }
    }

    fun loadSchedules(doctorId: Int) {
        viewModelScope.launch {
            _queueState.value = _queueState.value.copy(isLoadingSchedules = true, error = null)
            getSchedulesUseCase(doctorId)
                .onSuccess {
                    _queueState.value = _queueState.value.copy(
                        schedules = it,
                        isLoadingSchedules = false,
                    )
                }
                .onFailure {
                    _queueState.value = _queueState.value.copy(
                        isLoadingSchedules = false,
                        error = it.message,
                    )
                }
        }
    }

    fun loadRooms(hospitalId: Int) {
        // TODO: inject GetRoomsUseCase when HospitalModule provides it
        _roomsState.value = HospitalRoomsUiState(error = "Fitur kamar belum tersedia.")
    }

    fun bookQueue(
        scheduleId: Int,
        queueNumber: Int,
        scheduleDate: String,
        patientName: String,
        patientNik: String,
        patientBirthDate: String,
    ) {
        viewModelScope.launch {
            _bookingState.value = QueueBookingState.Loading
            val request = BookQueueRequest(
                scheduleId = scheduleId,
                queueNumber = queueNumber,
                scheduleDate = scheduleDate,
                patientName = patientName,
                patientNik = patientNik,
                patientBirthdate = patientBirthDate,
            )
            bookQueueUseCase(request)
                .onSuccess { queue ->
                    _bookingState.value = QueueBookingState.Success(queue.queueNumber)
                }
                .onFailure { e ->
                    _bookingState.value = QueueBookingState.Error(e.message)
                }
        }
    }

    fun resetBookingState() {
        _bookingState.value = QueueBookingState.Idle
    }

    fun loadMyQueues() {
        viewModelScope.launch {
            _myQueuesState.value = _myQueuesState.value.copy(isLoading = true, error = null)
            getMyQueuesUseCase()
                .onSuccess { list ->
                    _myQueuesState.value = MyQueuesUiState(
                        queues = list.map { q ->
                            BookedQueue(
                                id = q.id,
                                queueNumber = q.queueNumber,
                                scheduleDate = q.scheduleDate,
                                patientName = q.patientName,
                                patientNik = q.patientNik,
                                status = q.status,
                            )
                        },
                    )
                }
                .onFailure {
                    _myQueuesState.value = MyQueuesUiState(isLoading = false, error = it.message)
                }
        }
    }
}