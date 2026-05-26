package com.example.govix.hospital.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.hospital.domain.model.Hospital
import com.example.govix.hospital.domain.model.OperationalInfo
import com.example.govix.hospital.presentation.HospitalViewModel
import com.example.govix.hospital.ui.components.DetailTabChip
import com.example.govix.hospital.ui.components.HospitalPrimary
import com.example.govix.hospital.util.HospitalAssets

private val AccentYellow = Color(0xFFFCB216)

@Composable
fun HospitalDetailScreen(
    hospitalId: Int,
    viewModel: HospitalViewModel,
    onBack: () -> Unit,
    onQueueClick: (hospitalId: Int, hospitalName: String) -> Unit,
    onRoomsClick: (hospitalId: Int) -> Unit,
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(hospitalId) { viewModel.loadHospitalDetail(hospitalId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(AccentYellow),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.12f))
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Text(
                    text = state.hospital?.name.orEmpty(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                )
            }
        }

        when {
            state.isLoading && state.hospital == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentYellow)
                }
            }
            state.error != null && state.hospital == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.error ?: "", color = Color(0xFFE53935), modifier = Modifier.padding(24.dp))
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    // Hospital info card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(20.dp),
                    ) {
                        Text(
                            state.hospital?.name.orEmpty(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                        )
                        if (state.hospital?.city?.isNotBlank() == true) {
                            Spacer(Modifier.height(4.dp))
                            Text(state.hospital!!.city, fontSize = 13.sp, color = Color(0xFF9E9E9E))
                        }
                        if (state.hospital?.description?.isNotBlank() == true) {
                            Spacer(Modifier.height(8.dp))
                            Text(state.hospital!!.description, fontSize = 13.sp, color = Color(0xFF616161), lineHeight = 20.sp)
                        }
                    }

                    // Tab row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf("Layanan", "Operasional", "Ketentuan").forEachIndexed { i, label ->
                            DetailTabChip(label, selectedTab == i) { selectedTab = i }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        when (selectedTab) {
                            0 -> LayananTab(
                                onQueue = { onQueueClick(hospitalId, state.hospital?.name.orEmpty()) },
                                onRooms = { onRoomsClick(hospitalId) },
                            )
                            1 -> OperasionalTab(hospital = state.hospital)
                            2 -> KetentuanTab(info = state.operationalInfo)
                        }
                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LayananTab(onQueue: () -> Unit, onRooms: () -> Unit) {
    ActionCard(icon = Icons.Default.Groups, title = "Informasi Antrean Pasien", subtitle = "Daftar poli, dokter, dan jadwal", onClick = onQueue)
    Spacer(Modifier.height(12.dp))
    ActionCard(icon = Icons.Default.Bed, title = "Ketersediaan Kamar Rawat", subtitle = "Cek kamar tersedia secara real-time", onClick = onRooms)
}

@Composable
private fun ActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFF3D6)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = AccentYellow, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF9E9E9E))
        }
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFAAAAAA),
        )
    }
}

@Composable
private fun OperasionalTab(hospital: Hospital?) {
    val context = LocalContext.current
    val link = hospital?.website?.takeIf { it.isNotBlank() }
        ?: HospitalAssets.websiteFallback(hospital?.name)
        ?: "-"

    InfoCard("Link Layanan") {
        Text(
            text = link,
            color = AccentYellow,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                if (link != "-") context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            },
        )
    }
    Spacer(Modifier.height(12.dp))
    InfoCard("Alamat") { Text(hospital?.address.orEmpty().ifBlank { "-" }, fontSize = 13.sp, color = Color(0xFF616161)) }
    Spacer(Modifier.height(12.dp))
    InfoCard("Jam Operasional") { Text(HospitalAssets.operationalHoursFallback(hospital?.name), fontSize = 13.sp, color = Color(0xFF616161)) }
}

@Composable
private fun KetentuanTab(info: List<OperationalInfo>) {
    val benefits = info.filter { it.category == "benefit" }
    val requirements = info.filter { it.category == "requirement" }
    if (benefits.isEmpty() && requirements.isEmpty()) {
        InfoCard("Manfaat") { Text("Belum ada data.", fontSize = 13.sp, color = Color(0xFF9E9E9E)) }
        Spacer(Modifier.height(12.dp))
        InfoCard("Ketentuan") { Text("Belum ada data.", fontSize = 13.sp, color = Color(0xFF9E9E9E)) }
    } else {
        benefits.forEach { item ->
            InfoCard("Manfaat") { Text(item.content, fontSize = 13.sp, color = Color(0xFF616161)) }
            Spacer(Modifier.height(12.dp))
        }
        requirements.forEach { item ->
            InfoCard("Ketentuan") { Text(item.content, fontSize = 13.sp, color = Color(0xFF616161)) }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF9E9E9E))
        Spacer(Modifier.height(6.dp))
        content()
    }
}