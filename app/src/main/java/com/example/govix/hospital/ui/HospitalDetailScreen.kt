package com.example.govix.hospital.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.hospital.ui.components.DetailTabChip
import com.example.govix.hospital.ui.components.HospitalBlue
import com.example.govix.hospital.ui.components.HospitalBlueDark
import com.example.govix.hospital.ui.components.HospitalPrimary
import com.example.govix.hospital.util.HospitalAssets

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

    LaunchedEffect(hospitalId) {
        viewModel.loadHospitalDetail(hospitalId)
    }

    val hospital = state.hospital
    val name = hospital?.name.orEmpty()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        HospitalWaveHeader(title = name, onBack = onBack)

        if (state.isLoading && hospital == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (state.error != null && hospital == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = Color.Red, modifier = Modifier.padding(24.dp))
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(HospitalAssets.logoDrawableRes(name)),
                contentDescription = name,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hospital?.description.orEmpty(),
                    fontSize = 14.sp,
                    color = Color(0xFF616161),
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DetailTabChip("Layanan", selectedTab == 0) { selectedTab = 0 }
                    DetailTabChip("Operasional", selectedTab == 1) { selectedTab = 1 }
                    DetailTabChip("Ketentuan", selectedTab == 2) { selectedTab = 2 }
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (selectedTab) {
                    0 -> LayananTab(
                        onQueue = { onQueueClick(hospitalId, name) },
                        onRooms = { onRoomsClick(hospitalId) },
                    )
                    1 -> OperasionalTab(hospital = hospital)
                    2 -> KetentuanTab(info = state.operationalInfo)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun HospitalWaveHeader(title: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(HospitalBlue),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(HospitalBlueDark),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            )
            Icon(Icons.Default.BookmarkBorder, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun LayananTab(
    onQueue: () -> Unit,
    onRooms: () -> Unit,
) {
    DetailActionCard(icon = Icons.Default.Groups, title = "Informasi Antrean Pasien", onClick = onQueue)
    Spacer(modifier = Modifier.height(16.dp))
    DetailActionCard(icon = Icons.Default.Bed, title = "Ketersediaan Kamar Rawat", onClick = onRooms)
}

@Composable
private fun DetailActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.size(12.dp))
        Text(title)
    }
}

@Composable
private fun OperasionalTab(hospital: com.example.govix.data.remote.dto.HospitalDto?) {
    val context = LocalContext.current
    val name = hospital?.name
    val link = hospital?.website?.takeIf { it.isNotBlank() }
        ?: HospitalAssets.websiteFallback(name)
        ?: "-"
    val address = hospital?.address.orEmpty().ifBlank { "-" }
    val hours = HospitalAssets.operationalHoursFallback(name)

    InfoCard(title = "Link Layanan") {
        Text(
            text = link,
            color = HospitalPrimary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                if (link != "-") {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            },
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    InfoCard(title = "Alamat") { Text(address) }
    Spacer(modifier = Modifier.height(12.dp))
    InfoCard(title = "Jam Operasional") { Text(hours) }
}

@Composable
private fun KetentuanTab(info: List<com.example.govix.data.remote.dto.OperationalInfoDto>) {
    val benefits = info.filter { it.category == "benefit" }
    val requirements = info.filter { it.category == "requirement" }
    if (benefits.isEmpty() && requirements.isEmpty()) {
        InfoCard(title = "Manfaat") { Text("Isi detail akan ditampilkan di sini.") }
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard(title = "Pendaftaran Online") { Text("Isi detail akan ditampilkan di sini.") }
    } else {
        benefits.forEach { item ->
            InfoCard(title = "Manfaat") { Text(item.content.orEmpty()) }
            Spacer(modifier = Modifier.height(12.dp))
        }
        requirements.forEach { item ->
            InfoCard(title = "Ketentuan") { Text(item.content.orEmpty()) }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(title, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}
