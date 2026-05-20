package com.example.govix.hospital.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.data.remote.dto.RoomAvailabilityItemDto
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.hospital.ui.components.HospitalBlueHeader

@Composable
fun HospitalRoomsScreen(
    hospitalId: Int,
    viewModel: HospitalViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.roomsState.collectAsStateWithLifecycle()

    LaunchedEffect(hospitalId) {
        viewModel.loadRooms(hospitalId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        HospitalBlueHeader(title = "Ketersediaan Kamar Rawat", onBack = onBack)

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = Color.Red, modifier = Modifier.padding(24.dp))
            }
            return
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            Text(
                "Ketersediaan Kamar Rawat",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    RoomStatCard(
                        value = state.summary.total.toString(),
                        label = "Total Kamar Rawat",
                        valueColor = Color(0xFF212121),
                        icon = Icons.Default.Domain,
                        iconColor = Color(0xFF616161),
                        iconBg = Color(0xFFF5F5F5),
                    )
                }
                item {
                    RoomStatCard(
                        value = state.summary.available.toString(),
                        label = "Tersedia",
                        valueColor = Color(0xFF4CAF50),
                        icon = Icons.Default.HowToReg,
                        iconColor = Color(0xFF4CAF50),
                        iconBg = Color(0xFFE8F5E9),
                    )
                }
                item {
                    RoomStatCard(
                        value = state.summary.occupied.toString(),
                        label = "Terisi",
                        valueColor = Color(0xFFFF9800),
                        icon = Icons.Default.Groups,
                        iconColor = Color(0xFFFF9800),
                        iconBg = Color(0xFFFFF3E0),
                    )
                }
            }
            state.updatedAt?.let { updated ->
                Text(
                    text = "Update: $updated",
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.End,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Status Ketersediaan Ruangan", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            RoomTable(rooms = state.rooms)
        }
    }
}

@Composable
private fun RoomStatCard(
    value: String,
    label: String,
    valueColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBg: Color,
) {
    Row(
        modifier = Modifier
            .width(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = valueColor)
            Text(label, fontSize = 12.sp, color = Color(0xFF616161))
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg)
                .padding(8.dp),
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun RoomTable(rooms: List<RoomAvailabilityItemDto>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Ruang", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Kapasitas", modifier = Modifier.width(60.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Terisi", modifier = Modifier.width(50.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Tersedia", modifier = Modifier.width(60.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        rooms.forEach { room ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(roomColor(room.roomClass)),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(room.roomName.orEmpty(), modifier = Modifier.weight(1f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    (room.totalBeds ?: 0).toString(),
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                )
                Text(
                    (room.occupiedBeds ?: 0).toString(),
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = Color(0xFFFF9800),
                )
                Text(
                    (room.availableBeds ?: 0).toString(),
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = Color(0xFF4CAF50),
                )
            }
        }
    }
}

private fun roomColor(roomClass: String?): Color = when (roomClass?.uppercase()) {
    "ICU", "HCU" -> Color(0xFFE53935)
    "VIP", "VVIP" -> Color(0xFFFF9800)
    "KELAS I" -> Color(0xFF3F51B5)
    "KELAS II" -> Color(0xFF03A9F4)
    "KELAS III" -> Color(0xFF81D4FA)
    "ISOLASI" -> Color.Red
    else -> Color(0xFF9E9E9E)
}
