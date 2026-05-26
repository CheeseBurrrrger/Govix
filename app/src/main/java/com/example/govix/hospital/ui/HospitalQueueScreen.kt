package com.example.govix.hospital.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.core.data.ProfileDraftDataStore
import com.example.govix.core.util.parseDdMmYyyyToIsoOrNull
import com.example.govix.hospital.domain.model.Doctor
import com.example.govix.hospital.domain.model.DoctorSchedule
import com.example.govix.hospital.domain.model.Polyclinic
import com.example.govix.hospital.presentation.HospitalViewModel
import com.example.govix.hospital.presentation.QueueBookingState
import com.example.govix.hospital.ui.components.HospitalPrimary

private val AccentYellow = Color(0xFFFCB216)

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

    var selectedPoli by remember { mutableStateOf<Polyclinic?>(null) }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }
    var selectedSchedule by remember { mutableStateOf<DoctorSchedule?>(null) }
    var poliExpanded by remember { mutableStateOf(false) }
    var doctorExpanded by remember { mutableStateOf(false) }
    var scheduleExpanded by remember { mutableStateOf(false) }

    var scheduleDate by remember { mutableStateOf("") }
    var queueNumber by remember { mutableStateOf("1") }
    var patientName by remember { mutableStateOf("") }
    var patientNik by remember { mutableStateOf("") }
    var patientBirthDate by remember { mutableStateOf("") }

    LaunchedEffect(hospitalId) { viewModel.loadPolyclinics(hospitalId) }

    LaunchedEffect(Unit) {
        val draft = ProfileDraftDataStore(context.applicationContext).readDraftOrNull()
        if (draft != null) {
            val name = draft.fullName.ifBlank {
                listOf(draft.firstName, draft.lastName).filter { it.isNotBlank() }.joinToString(" ")
            }
            if (patientName.isBlank()) patientName = name
            if (patientNik.isBlank()) patientNik = draft.nik
            if (patientBirthDate.isBlank()) patientBirthDate = draft.birthDate
        }
    }

    LaunchedEffect(bookingState) {
        when (val s = bookingState) {
            is QueueBookingState.Success -> {
                Toast.makeText(context, "Booking berhasil. Nomor antrean: ${s.queueNumber}", Toast.LENGTH_LONG).show()
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
            .background(Color(0xFFF7F7F7)),
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AccentYellow)
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
                Text("Daftar Antrean", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        if (state.isLoadingPolyclinics && state.polyclinics.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(color = AccentYellow)
            }
            return
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Hospital name
            SectionCard {
                FieldLabel("Rumah Sakit")
                Text(hospitalName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }

            // Polyclinic
            SectionCard {
                FieldLabel("Pilih Poli")
                ExposedDropdownMenuBox(expanded = poliExpanded, onExpandedChange = { poliExpanded = it }) {
                    OutlinedTextField(
                        value = selectedPoli?.name ?: "— Pilih Poli —",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = poliExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                    )
                    ExposedDropdownMenu(expanded = poliExpanded, onDismissRequest = { poliExpanded = false }) {
                        state.polyclinics.forEach { poli ->
                            DropdownMenuItem(
                                text = { Text(poli.name) },
                                onClick = {
                                    selectedPoli = poli; selectedDoctor = null; selectedSchedule = null
                                    poliExpanded = false; viewModel.loadDoctors(poli.id)
                                },
                            )
                        }
                    }
                }
            }

            // Doctor
            SectionCard {
                FieldLabel("Pilih Dokter")
                ExposedDropdownMenuBox(expanded = doctorExpanded, onExpandedChange = { doctorExpanded = it }) {
                    OutlinedTextField(
                        value = selectedDoctor?.let { formatDoctor(it) } ?: "— Pilih Dokter —",
                        onValueChange = {},
                        readOnly = true,
                        enabled = selectedPoli != null && !state.isLoadingDoctors,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = doctorExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                    )
                    ExposedDropdownMenu(expanded = doctorExpanded, onDismissRequest = { doctorExpanded = false }) {
                        state.doctors.forEach { doctor ->
                            DropdownMenuItem(
                                text = { Text(formatDoctor(doctor)) },
                                onClick = {
                                    selectedDoctor = doctor; selectedSchedule = null
                                    doctorExpanded = false; viewModel.loadSchedules(doctor.id)
                                },
                            )
                        }
                    }
                }
            }

            // Schedule
            SectionCard {
                FieldLabel("Pilih Jadwal")
                ExposedDropdownMenuBox(expanded = scheduleExpanded, onExpandedChange = { scheduleExpanded = it }) {
                    OutlinedTextField(
                        value = selectedSchedule?.let { formatSchedule(it) } ?: "— Pilih Jadwal —",
                        onValueChange = {},
                        readOnly = true,
                        enabled = selectedDoctor != null && !state.isLoadingSchedules,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = scheduleExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                    )
                    ExposedDropdownMenu(expanded = scheduleExpanded, onDismissRequest = { scheduleExpanded = false }) {
                        state.schedules.forEach { schedule ->
                            val label = formatSchedule(schedule)
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(label, fontWeight = FontWeight.SemiBold)
                                        if (schedule.isFull) {
                                            Text("Penuh", fontSize = 11.sp, color = Color(0xFFE53935))
                                        }
                                    }
                                },
                                onClick = { if (!schedule.isFull) { selectedSchedule = schedule; scheduleExpanded = false } },
                                enabled = !schedule.isFull,
                            )
                        }
                    }
                }
            }

            // Patient data
            SectionCard {
                FieldLabel("Data Pasien")
                OutlinedTextField(
                    value = patientName, onValueChange = { patientName = it },
                    label = { Text("Nama Pasien") }, modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = patientNik, onValueChange = { patientNik = it },
                    label = { Text("NIK (16 digit)") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = patientBirthDate, onValueChange = { patientBirthDate = it },
                    label = { Text("Tanggal Lahir (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                )
            }

            // Booking data
            SectionCard {
                FieldLabel("Data Booking")
                OutlinedTextField(
                    value = scheduleDate, onValueChange = { scheduleDate = it },
                    label = { Text("Tanggal Berobat (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = queueNumber, onValueChange = { queueNumber = it },
                    label = { Text("Nomor Antrean") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentYellow),
                )
            }

            Button(
                onClick = {
                    val sid = selectedSchedule?.id ?: return@Button
                    val qn = queueNumber.trim().toIntOrNull()
                    val sdIso = parseDdMmYyyyToIsoOrNull(scheduleDate) ?: scheduleDate.trim()
                    val bdRaw = patientBirthDate.trim()
                    val bdIso = if (Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(bdRaw)) bdRaw
                    else parseDdMmYyyyToIsoOrNull(bdRaw) ?: bdRaw
                    when {
                        patientName.isBlank() -> Toast.makeText(context, "Nama pasien wajib diisi.", Toast.LENGTH_LONG).show()
                        patientNik.length != 16 || !patientNik.all { it.isDigit() } -> Toast.makeText(context, "NIK harus 16 digit.", Toast.LENGTH_LONG).show()
                        !Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(bdIso) -> Toast.makeText(context, "Format tanggal lahir salah.", Toast.LENGTH_LONG).show()
                        !Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(sdIso) -> Toast.makeText(context, "Format tanggal berobat salah.", Toast.LENGTH_LONG).show()
                        qn == null || qn <= 0 -> Toast.makeText(context, "Nomor antrean tidak valid.", Toast.LENGTH_LONG).show()
                        else -> viewModel.bookQueue(sid, qn, sdIso, patientName, patientNik, bdIso)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = selectedPoli != null && selectedDoctor != null && selectedSchedule != null
                        && bookingState !is QueueBookingState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = AccentYellow),
                shape = RoundedCornerShape(14.dp),
            ) {
                if (bookingState is QueueBookingState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Konfirmasi Booking", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = content,
    )
}

@Composable
private fun FieldLabel(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF9E9E9E))
    Spacer(Modifier.height(4.dp))
}

private fun formatDoctor(doctor: Doctor): String {
    val title = doctor.title.takeIf { it.isNotBlank() }?.let { " ($it)" }.orEmpty()
    return "${doctor.name}$title"
}

private fun formatSchedule(schedule: DoctorSchedule): String {
    val start = schedule.startTime.take(5)
    val end = schedule.endTime.take(5)
    return "${schedule.dayOfWeek} · $start–$end"
}