package com.example.govix.hospital.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.data.remote.dto.HospitalDto
import com.example.govix.hospital.presentation.HospitalViewModel
import com.example.govix.hospital.ui.components.ServiceLayananCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalListScreen(
    viewModel: HospitalViewModel,
    onHospitalClick: (hospitalId: Int, hospitalName: String) -> Unit,
    onEmergencyClick: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.listState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (state.hospitals.isEmpty()) viewModel.loadHospitals()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Layanan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                state.isLoading && state.hospitals.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null && state.hospitals.isEmpty() -> {
                    Text(
                        text = state.error ?: "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        color = Color.Red,
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Layanan Kesehatan",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        state.hospitals.forEach { hospital ->
                            HospitalListItem(
                                hospital = hospital,
                                onClick = {
                                    val id = hospital.id ?: return@HospitalListItem
                                    onHospitalClick(id, hospital.name.orEmpty())
                                },
                            )
                        }
                        Text(
                            text = "Layanan Lainnya",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                        ServiceLayananCard(title = "Nomor Darurat", onClick = onEmergencyClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun HospitalListItem(
    hospital: HospitalDto,
    onClick: () -> Unit,
) {
    val title = buildString {
        append(hospital.name.orEmpty())
        hospital.city?.takeIf { it.isNotBlank() }?.let { append(" · $it") }
    }
    ServiceLayananCard(title = title, onClick = onClick)
}
