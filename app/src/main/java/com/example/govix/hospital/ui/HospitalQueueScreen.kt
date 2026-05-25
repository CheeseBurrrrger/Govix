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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.core.data.ProfileDraftDataStore
import com.example.govix.core.util.parseDdMmYyyyToIsoOrNull
import com.example.govix.data.remote.dto.DoctorDto
import com.example.govix.data.remote.dto.DoctorScheduleDto
import com.example.govix.data.remote.dto.PolyclinicDto
import com.example.govix.hospital.presentation.HospitalViewModel
import com.example.govix.hospital.QueueBookingState
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
    val bookingState by viewModel.bookingState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var selectedPoli by remember { mutableStateOf<PolyclinicDto?>(null) }
    var selectedDoctor by remember { mutableStateOf<DoctorDto?>(null) }
    var selectedSchedule by remember { mutableStateOf<DoctorScheduleDto?>(null) }
    var poliExpanded by remember { mutableStateOf(false) }
    var doctorExpanded by remember { mutableStateOf(false) }
    var scheduleExpanded by remember { mutableStateOf(false) }

    var scheduleDate by remember { mutableStateOf("") }
    var queueNumber by remember { mutableStateOf("1") }
    var patientName by remember { mutableStateOf("") }
    var patientNik by remember { mutableStateOf("") }
    var patientBirthDate by remember { mutableStateOf("") }

    LaunchedEffect(hospitalId) {
        viewModel.loadPolyclinics(hospitalId)
    }

    LaunchedEffect(Unit) {
        val draft = ProfileDraftDataStore(context.applicationContext).readDraftOrNull()
        if (draft != null) {
            val fallbackName = draft.fullName.ifBlank {
                listOf(draft.firstName, draft.lastName).filter { it.isNotBlank() }.joinToString(" ")
            }
            if (patientName.isBlank()) patientName = fallbackName
            if (patientNik.isBlank()) patientNik = draft.nik
            if (patientBirthDate.isBlank()) patientBirthDate = draft.birthDate
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(bookingState) {
        when (val s = bookingState) {
            is QueueBookingState.Success -> {
                Toast.makeText(
                    context,
                    "Booking berhasil. Nomor antrean: ${s.queue.queueNumber ?: "-"}",
                    Toast.LENGTH_LONG,
                ).show()
                viewModel.resetBookingState()
            }
            is QueueBookingState.Error -> {
                Toast.makeText(context, s.message ?: "Booking gagal.", Toast.LENGTH_LONG).show()
                viewModel.resetBookingState()
            }
            else -> Unit
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

            Text("Data Pasien", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = { Text("Nama Pasien") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = patientNik,
                onValueChange = { patientNik = it },
                label = { Text("NIK Pasien") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = patientBirthDate,
                onValueChange = { patientBirthDate = it },
                label = { Text("Tanggal Lahir Pasien (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Data Booking", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = scheduleDate,
                onValueChange = { scheduleDate = it },
                label = { Text("Tanggal Berobat (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = queueNumber,
                onValueChange = { queueNumber = it },
                label = { Text("Nomor Antrean") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val scheduleId = selectedSchedule?.id
                    if (scheduleId == null) return@Button
                    val qn = queueNumber.trim().toIntOrNull()
                    val scheduleDateIso = parseDdMmYyyyToIsoOrNull(scheduleDate) ?: scheduleDate.trim()
                    val birthDateIso = run {
                        val s = patientBirthDate.trim()
                        if (s.length >= 10 && Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(s.substring(0, 10))) s.substring(0, 10)
                        else parseDdMmYyyyToIsoOrNull(s) ?: s
                    }
                    when {
                        patientName.isBlank() -> Toast.makeText(context, "Nama pasien wajib diisi.", Toast.LENGTH_LONG).show()
                        patientNik.length != 16 || !patientNik.all { it.isDigit() } -> Toast.makeText(context, "NIK pasien harus 16 digit.", Toast.LENGTH_LONG).show()
                        patientBirthDate.isBlank() -> Toast.makeText(context, "Tanggal lahir pasien wajib diisi.", Toast.LENGTH_LONG).show()
                        !Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(birthDateIso) -> Toast.makeText(context, "Tanggal lahir pasien harus YYYY-MM-DD atau DD/MM/YYYY.", Toast.LENGTH_LONG).show()
                        scheduleDate.isBlank() -> Toast.makeText(context, "Tanggal berobat wajib diisi.", Toast.LENGTH_LONG).show()
                        !Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(scheduleDateIso) -> Toast.makeText(context, "Tanggal berobat harus YYYY-MM-DD atau DD/MM/YYYY.", Toast.LENGTH_LONG).show()
                        qn == null || qn <= 0 -> Toast.makeText(context, "Nomor antrean tidak valid.", Toast.LENGTH_LONG).show()
                        else -> viewModel.bookQueue(
                            scheduleId = scheduleId,
                            queueNumber = qn,
                            scheduleDate = scheduleDateIso,
                            patientName = patientName,
                            patientNik = patientNik,
                            patientBirthDate = birthDateIso,
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = selectedPoli != null && selectedDoctor != null && selectedSchedule != null && bookingState !is QueueBookingState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = HospitalPrimary),
                shape = RoundedCornerShape(12.dp),
            ) {
                if (bookingState is QueueBookingState.Loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Konfirmasi", fontWeight = FontWeight.Bold, color = Color.White)
                }
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
