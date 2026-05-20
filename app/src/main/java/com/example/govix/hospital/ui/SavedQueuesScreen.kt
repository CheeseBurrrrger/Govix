package com.example.govix.hospital.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.data.remote.dto.QueueDto
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.hospital.ui.components.HospitalBlueHeader
import com.example.govix.hospital.ui.components.HospitalPrimary

@Composable
fun SavedQueuesScreen(
    viewModel: HospitalViewModel,
) {
    val state by viewModel.myQueuesState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.loadMyQueues() }
    LaunchedEffect(state.error) {
        state.error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        HospitalBlueHeader(
            title = "Tersimpan",
            onBack = { /* no-op */ },
            showBookmark = false,
            showBack = false,
        )
        Spacer(Modifier.padding(top = 8.dp))

        when {
            state.isLoading && state.queues.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = HospitalPrimary)
                }
            }
            state.queues.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Belum ada booking antrean.", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.padding(8.dp))
                    Button(
                        onClick = { viewModel.loadMyQueues() },
                        colors = ButtonDefaults.buttonColors(containerColor = HospitalPrimary),
                    ) {
                        Text("Refresh", color = Color.White)
                    }
                }
            }
            else -> {
                Button(
                    onClick = { viewModel.loadMyQueues() },
                    colors = ButtonDefaults.buttonColors(containerColor = HospitalPrimary),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Refresh", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.padding(top = 12.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.queues) { queue ->
                        SavedQueueCard(queue)
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedQueueCard(queue: QueueDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF3D6), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Nomor Antrean: ${queue.queueNumber ?: "-"}",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF212121),
        )
        Text("Tanggal: ${queue.scheduleDate ?: "-"}", color = Color(0xFF616161), fontSize = 13.sp)
        Text("Status: ${queue.status ?: "-"}", color = Color(0xFF616161), fontSize = 13.sp)
        Text("Pasien: ${queue.patientName ?: "-"}", color = Color(0xFF616161), fontSize = 13.sp)
        Text("NIK: ${queue.patientNik ?: "-"}", color = Color(0xFF616161), fontSize = 13.sp)
    }
}
