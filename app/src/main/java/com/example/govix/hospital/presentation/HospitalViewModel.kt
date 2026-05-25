package com.example.govix.hospital.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.model.HospitalDetail
import com.example.govix.hospital.domain.model.Polyclinic
import com.example.govix.hospital.domain.usecase.GetDoctorsUseCase
import com.example.govix.hospital.domain.usecase.GetHospitalDetailUseCase
import com.example.govix.hospital.domain.usecase.GetHospitalsUseCase
import com.example.govix.hospital.domain.usecase.GetPolyclinicsUseCase
import com.example.govix.hospital.domain.usecase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HospitalListState{
    object Idle : HospitalListState()
    object  Loading : HospitalListState()
    data class Success(val hospitals: List<Hospital>) : HospitalListState()
    data class Error(val message: String?): HospitalListState()
}


sealed class HospitalDetailState{
    object Idle : HospitalDetailState()
    object  Loading : HospitalDetailState()
    data class Success(val detail: HospitalDetail) : HospitalDetailState()
    data class Error(val message: String?): HospitalDetailState()
}

sealed class PolyclinicState{
    object Idle : PolyclinicState()
    object  Loading : PolyclinicState()
    data class Success(val detail: List<Polyclinic>) : PolyclinicState()
    data class Error(val message: String?): PolyclinicState()
}

sealed class DoctorState{
    object Idle : DoctorState()
    object  Loading : DoctorState()
    data class Success(val detail: List<Doctor>) : DoctorState()
    data class Error(val message: String?): DoctorState()
}

sealed class ScheduleState {
    object Idle : ScheduleState()
    object  Loading : ScheduleState()
    data class Success(val detail: List<DoctorSchedule>) : ScheduleState()
    data class Error(val message: String?): ScheduleState()
}

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val getHospitalsUseCase: GetHospitalsUseCase,
    private val getHospitalDetailUseCase: GetHospitalDetailUseCase,
    private val getPolyclinicsUseCase: GetPolyclinicsUseCase,
    private val getDoctorsUseCase: GetDoctorsUseCase,
    private val getSchedulesUseCase: GetSchedulesUseCase,
) : ViewModel() {

    private val _hospitalListState = MutableStateFlow<HospitalListState>(HospitalListState.Idle)
    val hospitalListState: StateFlow<HospitalListState> = _hospitalListState.asStateFlow()

    private val _hospitalDetailState = MutableStateFlow<HospitalDetailState>(HospitalDetailState.Idle)
    val hospitalDetailState: StateFlow<HospitalDetailState> = _hospitalDetailState.asStateFlow()

    private val _polyclinicState = MutableStateFlow<PolyclinicState>(PolyclinicState.Idle)
    val polyclinicState: StateFlow<PolyclinicState> = _polyclinicState.asStateFlow()

    private val _doctorState = MutableStateFlow<DoctorState>(DoctorState.Idle)
    val doctorState: StateFlow<DoctorState> = _doctorState.asStateFlow()

    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Idle)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState.asStateFlow()

    fun loadHospitals(city: String? = null) {
        viewModelScope.launch {
            _hospitalListState.value = HospitalListState.Loading
            getHospitalsUseCase(city)
                .onSuccess { _hospitalListState.value = HospitalListState.Success(it) }
                .onFailure { _hospitalListState.value = HospitalListState.Error(it.message) }
        }
    }

    fun loadHospitalDetail(id: Int) {
        viewModelScope.launch {
            _hospitalDetailState.value = HospitalDetailState.Loading
            getHospitalDetailUseCase(id)
                .onSuccess { _hospitalDetailState.value = HospitalDetailState.Success(it) }
                .onFailure { _hospitalDetailState.value = HospitalDetailState.Error(it.message) }
        }
    }

    fun loadPolyclinics(hospitalId: Int) {
        viewModelScope.launch {
            _polyclinicState.value = PolyclinicState.Loading
            getPolyclinicsUseCase(hospitalId)
                .onSuccess { _polyclinicState.value = PolyclinicState.Success(it) }
                .onFailure { _polyclinicState.value = PolyclinicState.Error(it.message) }
        }
    }

    fun loadDoctors(polyclinicId: Int) {
        viewModelScope.launch {
            _doctorState.value = DoctorState.Loading
            getDoctorsUseCase(polyclinicId)
                .onSuccess { _doctorState.value = DoctorState.Success(it) }
                .onFailure { _doctorState.value = DoctorState.Error(it.message) }
        }
    }

    fun loadSchedules(doctorId: Int) {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            getSchedulesUseCase(doctorId)
                .onSuccess { _scheduleState.value = ScheduleState.Success(it) }
                .onFailure { _scheduleState.value = ScheduleState.Error(it.message) }
        }
    }
}