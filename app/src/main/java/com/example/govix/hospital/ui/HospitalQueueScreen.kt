package com.example.govix.hospital.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.data.remote.dto.DoctorDto
import com.example.govix.data.remote.dto.DoctorScheduleDto
import com.example.govix.data.remote.dto.PolyclinicDto
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.hospital.ui.components.HospitalBlueHeader
import com.example.govix.hospital.ui.components.HospitalPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalQueueScreen(
    hospitalId: Int,
    hospitalName: String,
    viewModel: HospitalViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.queueState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var selectedPoli by remember { mutableStateOf<PolyclinicDto?>(null) }
    var selectedDoctor by remember { mutableStateOf<DoctorDto?>(null) }
    var selectedSchedule by remember { mutableStateOf<DoctorScheduleDto?>(null) }
    var poliExpanded by remember { mutableStateOf(false) }
    var doctorExpanded by remember { mutableStateOf(false) }
    var scheduleExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(hospitalId) {
        viewModel.loadPolyclinics(hospitalId)
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        HospitalBlueHeader(title = "Informasi Antrean Pasien", onBack = onBack)

        if (state.isLoadingPolyclinics && state.polyclinics.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            return
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            Text("Rumah Sakit", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(hospitalName, modifier = Modifier.padding(bottom = 16.dp))

            Text("Pilih Poli", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = poliExpanded, onExpandedChange = { poliExpanded = it }) {
                OutlinedTextField(
                    value = selectedPoli?.name ?: "-Pilih-",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = poliExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(expanded = poliExpanded, onDismissRequest = { poliExpanded = false }) {
                    state.polyclinics.forEach { poli ->
                        DropdownMenuItem(
                            text = { Text(poli.name.orEmpty()) },
                            onClick = {
                                selectedPoli = poli
                                selectedDoctor = null
                                selectedSchedule = null
                                poliExpanded = false
                                poli.id?.let { viewModel.loadDoctors(it) }
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Pilih Dokter", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = doctorExpanded, onExpandedChange = { doctorExpanded = it }) {
                OutlinedTextField(
                    value = selectedDoctor?.let { formatDoctor(it) } ?: "-Pilih-",
                    onValueChange = {},
                    readOnly = true,
                    enabled = selectedPoli != null && !state.isLoadingDoctors,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = doctorExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(expanded = doctorExpanded, onDismissRequest = { doctorExpanded = false }) {
                    state.doctors.forEach { doctor ->
                        DropdownMenuItem(
                            text = { Text(formatDoctor(doctor)) },
                            onClick = {
                                selectedDoctor = doctor
                                selectedSchedule = null
                                doctorExpanded = false
                                doctor.id?.let { viewModel.loadSchedules(it) }
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Pilih Jadwal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = scheduleExpanded, onExpandedChange = { scheduleExpanded = it }) {
                OutlinedTextField(
                    value = selectedSchedule?.let { formatSchedule(it) } ?: "-Pilih-",
                    onValueChange = {},
                    readOnly = true,
                    enabled = selectedDoctor != null && !state.isLoadingSchedules,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = scheduleExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(expanded = scheduleExpanded, onDismissRequest = { scheduleExpanded = false }) {
                    state.schedules.forEach { schedule ->
                        DropdownMenuItem(
                            text = { Text(formatSchedule(schedule)) },
                            onClick = {
                                selectedSchedule = schedule
                                scheduleExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "Lanjut ke data pasien — booking antrean API menyusul.",
                        Toast.LENGTH_LONG,
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = selectedPoli != null && selectedDoctor != null && selectedSchedule != null,
                colors = ButtonDefaults.buttonColors(containerColor = HospitalPrimary),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Konfirmasi", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

private fun formatDoctor(doctor: DoctorDto): String {
    val title = doctor.title?.takeIf { it.isNotBlank() }?.let { " ($it)" }.orEmpty()
    return "${doctor.name.orEmpty()}$title"
}

private fun formatSchedule(schedule: DoctorScheduleDto): String {
    val day = schedule.dayOfWeek.orEmpty()
    val start = schedule.startTime?.take(5).orEmpty()
    val end = schedule.endTime?.take(5).orEmpty()
    return "$day · $start - $end"
}
