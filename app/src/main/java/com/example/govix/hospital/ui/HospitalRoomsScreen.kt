package com.example.govix.hospital.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.hospital.domain.model.Room
import com.example.govix.hospital.presentation.HospitalViewModel

private val AccentYellow = Color(0xFFFCB216)

@Composable
fun HospitalRoomsScreen(
    hospitalId: Int,
    viewModel: HospitalViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.roomsState.collectAsStateWithLifecycle()

    LaunchedEffect(hospitalId) { viewModel.loadRooms(hospitalId) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(AccentYellow).padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
                Text("Ketersediaan Kamar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentYellow)
            }
            state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = Color(0xFFE53935), modifier = Modifier.padding(24.dp))
            }
            else -> {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            RoomStatCard("${state.summary.total}", "Total Kamar", Color(0xFF1A1A1A), Icons.Default.Domain, Color(0xFF616161), Color(0xFFF5F5F5))
                        }
                        item {
                            RoomStatCard("${state.summary.available}", "Tersedia", Color(0xFF4CAF50), Icons.Default.HowToReg, Color(0xFF4CAF50), Color(0xFFE8F5E9))
                        }
                        item {
                            RoomStatCard("${state.summary.occupied}", "Terisi", Color(0xFFFF9800), Icons.Default.Groups, Color(0xFFFF9800), Color(0xFFFFF3E0))
                        }
                    }
                    state.updatedAt?.let {
                        Text("Update: $it", fontSize = 11.sp, color = Color(0xFF9E9E9E), modifier = Modifier.fillMaxWidth().padding(top = 8.dp), textAlign = TextAlign.End)
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Status Ruangan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    Spacer(Modifier.height(12.dp))
                    RoomTable(state.rooms)
                }
            }
        }
    }
}

@Composable
private fun RoomStatCard(value: String, label: String, valueColor: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, iconBg: Color) {
    Row(
        modifier = Modifier.width(180.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = valueColor)
            Text(label, fontSize = 11.sp, color = Color(0xFF9E9E9E))
        }
        Box(modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(iconBg).padding(8.dp)) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun RoomTable(rooms: List<Room>) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Ruang", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF9E9E9E))
            Text("Total", Modifier.width(50.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF9E9E9E))
            Text("Terisi", Modifier.width(50.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF9E9E9E))
            Text("Kosong", Modifier.width(55.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF9E9E9E))
        }
        Spacer(Modifier.height(12.dp))
        rooms.forEach { room ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(roomColor(room.roomClass)))
                Spacer(Modifier.width(10.dp))
                Text(room.roomName, Modifier.weight(1f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("${room.totalBeds}", Modifier.width(50.dp), textAlign = TextAlign.Center, fontSize = 13.sp)
                Text("${room.occupiedBeds}", Modifier.width(50.dp), textAlign = TextAlign.Center, fontSize = 13.sp, color = Color(0xFFFF9800))
                Text("${room.availableBeds}", Modifier.width(55.dp), textAlign = TextAlign.Center, fontSize = 13.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}

private fun roomColor(roomClass: String): Color = when (roomClass.uppercase()) {
    "ICU", "HCU" -> Color(0xFFE53935)
    "VIP", "VVIP" -> Color(0xFFFF9800)
    "KELAS I" -> Color(0xFF3F51B5)
    "KELAS II" -> Color(0xFF03A9F4)
    "KELAS III" -> Color(0xFF81D4FA)
    "ISOLASI" -> Color(0xFFE53935)
    else -> Color(0xFF9E9E9E)
}