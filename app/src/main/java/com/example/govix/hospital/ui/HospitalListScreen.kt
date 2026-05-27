package com.example.govix.hospital.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.presentation.HospitalViewModel
import com.example.govix.hospital.ui.components.HospitalPrimary
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.govix.hospital.util.HospitalAssets

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
                title = { Text("Layanan Kesehatan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HospitalPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(padding),
        ) {
            when {
                state.isLoading && state.hospitals.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = HospitalPrimary,
                    )
                }
                state.error != null && state.hospitals.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(state.error ?: "", color = Color(0xFFE53935))
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.loadHospitals() },
                            colors = ButtonDefaults.buttonColors(containerColor = HospitalPrimary),
                        ) { Text("Coba Lagi", color = Color.White) }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        state.hospitals.forEach { hospital ->
                            HospitalCard(
                                hospital = hospital,
                                onClick = { onHospitalClick(hospital.id, hospital.name) },
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Layanan Lainnya",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                        )
                        EmergencyCard(onClick = onEmergencyClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun HospitalCard(hospital: Hospital, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFF3D6)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(HospitalAssets.logoDrawableRes(hospital.name)),
                contentDescription = hospital.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(hospital.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
            if (hospital.city.isNotBlank()) {
                Text(hospital.city, fontSize = 12.sp, color = Color(0xFF9E9E9E))
            }
        }
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFAAAAAA),
        )
    }
}

@Composable
private fun EmergencyCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFEBEB))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFCDD2)),
            contentAlignment = Alignment.Center,
        ) {
            Text("🚨", fontSize = 20.sp)
        }
        Spacer(Modifier.width(14.dp))
        Text("Nomor Darurat", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFFE53935))
    }
}