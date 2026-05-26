package com.example.govix.hospital.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.hospital.presentation.BookedQueue
import com.example.govix.hospital.presentation.HospitalViewModel

private val AccentYellow = Color(0xFFFCB216)

@Composable
fun SavedQueuesScreen(viewModel: HospitalViewModel) {
    val state by viewModel.myQueuesState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadMyQueues() }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7))) {
        Box(
            modifier = Modifier.fillMaxWidth().background(AccentYellow).padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            Text("Antrean Saya", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        }

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentYellow)
                }
            }
            state.queues.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("🏥", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("Belum ada antrean.", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                    Spacer(Modifier.height(4.dp))
                    Text("Booking antrean di RS terdekat.", fontSize = 13.sp, color = Color(0xFF9E9E9E))
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.loadMyQueues() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentYellow),
                        shape = RoundedCornerShape(12.dp),
                    ) { Text("Refresh", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { viewModel.loadMyQueues() }) {
                        Text("Refresh", color = AccentYellow, fontWeight = FontWeight.SemiBold)
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.queues) { queue -> QueueCard(queue) }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun QueueCard(queue: BookedQueue) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "No. ${queue.queueNumber}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = AccentYellow,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor(queue.status).copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            ) {
                Text(queue.status, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = statusColor(queue.status))
            }
        }
        Text(queue.patientName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
        Text("NIK: ${queue.patientNik}", fontSize = 12.sp, color = Color(0xFF9E9E9E))
        Text("Tanggal: ${queue.scheduleDate}", fontSize = 12.sp, color = Color(0xFF9E9E9E))
    }
}

private fun statusColor(status: String): Color = when (status.lowercase()) {
    "waiting", "menunggu" -> Color(0xFFFF9800)
    "done", "selesai"     -> Color(0xFF4CAF50)
    "cancelled", "batal"  -> Color(0xFFE53935)
    else                  -> Color(0xFF9E9E9E)
}