package com.example.govix.hospital.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.govix.hospital.ui.components.HospitalBlueHeader

@Composable
fun EmergencyScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        HospitalBlueHeader(title = "Nomor Darurat", onBack = onBack, showBookmark = false)
        Text(
            text = "Polisi: 110\nAmbulans: 118\nPemadam: 113",
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp),
        )
    }
}
