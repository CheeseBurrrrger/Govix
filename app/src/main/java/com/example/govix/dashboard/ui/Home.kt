// dashboard/ui/Home.kt
package com.example.govix.dashboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.govix.R
import com.example.govix.hospital.presentation.HospitalListUiState
import com.example.govix.hospital.presentation.HospitalViewModel
import com.example.govix.navigation.Screen
import java.util.Calendar

private val Yellow      = Color(0xFFFCB216)
private val YellowLight = Color(0xFFFDD06A)
private val YellowDeep  = Color(0xFFE09A00)
private val Surface     = Color(0xFFF7F7F7)
private val CardBg      = Color.White
private val TextPrimary = Color(0xFF1A1A1A)
private val TextHint    = Color(0xFFAAAAAA)

private data class ServiceItem(
    val name: String,
    val iconRes: Int,
    val bgColor: Color = Color(0xFFFFF3D6),
    val isHospital: Boolean = false,
    val isEmergency: Boolean = false,
)

private val quickServices = listOf(
    ServiceItem("Darurat",   R.drawable.logo_aplikasi, Color(0xFFFFEBEB), isEmergency = true),
    ServiceItem("Daftar RS", R.drawable.layanan,       Color(0xFFE8F4FF), isHospital  = true),
    ServiceItem("Dr.Soetomo",R.drawable.rsud_soetomo,  Color(0xFFE8FFE8), isHospital  = true),
    ServiceItem("Saiful A.", R.drawable.rsud_saiful,   Color(0xFFFFF3D6), isHospital  = true),
    ServiceItem("Karsa H.",  R.drawable.rsud_karsa,    Color(0xFFF3E8FF), isHospital  = true),
    ServiceItem("RSUD Haji", R.drawable.rsud_haji,     Color(0xFFE8F4FF), isHospital  = true),
)

// ── Root screen ───────────────────────────────────────────────────────────────
@Composable
fun DashboardHomeScreen(
    navController: NavController,
    hospitalViewModel: HospitalViewModel,
    userName: String = "Pengunjung",
    onLogoutClick: (() -> Unit)? = null,
) {
    val listState by hospitalViewModel.listState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .verticalScroll(rememberScrollState())
    ) {
        HeroHeader(userName = userName, onLogoutClick = onLogoutClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
                .background(Surface, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(top = 24.dp)
        ) {
            SectionLabel("Layanan Cepat")
            Spacer(Modifier.height(12.dp))
            ServiceGrid(
                services = quickServices,
                onServiceClick = { service ->
                    when {
                        service.isEmergency -> navController.navigate(Screen.Emergency)
                        service.name == "Daftar RS" -> navController.navigate(Screen.HospitalList)
                        service.isHospital -> {
                            val match = (listState as? HospitalListUiState)
                                ?.hospitals
                                ?.firstOrNull {
                                    it.name?.contains(service.name.split(" ").first(), ignoreCase = true) == true ||
                                            it.shortName?.contains(service.name.split(" ").first(), ignoreCase = true) == true
                                }
                            if (match?.id != null) {
                                navController.navigate(Screen.hospitalDetail(match.id))
                            } else {
                                navController.navigate(Screen.HospitalList)
                            }
                        }
                    }
                }
            )

            Spacer(Modifier.height(28.dp))
            BookingBanner(
                onFindHospital = { navController.navigate(Screen.HospitalList) },
                onSaved        = { navController.navigate(Screen.Saved) },
            )
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun HeroHeader(
    userName: String,
    onLogoutClick: (() -> Unit)?,
) {
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = when {
        hour < 11 -> "Selamat pagi"
        hour < 15 -> "Selamat siang"
        hour < 18 -> "Selamat sore"
        else      -> "Selamat malam"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(YellowDeep, Yellow, YellowLight)))
            .padding(bottom = 36.dp)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.personn),
                        contentDescription = null,
                        tint = Yellow,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(greeting, fontSize = 12.sp, color = Color(0xFF5A3A00), fontWeight = FontWeight.Medium)
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A0A00),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (onLogoutClick != null) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.12f))
                        .clickable { onLogoutClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = Color(0xFF1A0A00), modifier = Modifier.size(20.dp))
                    Text("Keluar", fontSize = 10.sp, color = Color(0xFF1A0A00), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(title: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        if (actionLabel != null && onAction != null) {
            Row(modifier = Modifier.clickable { onAction() }, verticalAlignment = Alignment.CenterVertically) {
                Text(actionLabel, fontSize = 13.sp, color = Yellow, fontWeight = FontWeight.SemiBold)
                Icon(Icons.Outlined.ChevronRight, null, tint = Yellow, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun ServiceGrid(services: List<ServiceItem>, onServiceClick: (ServiceItem) -> Unit) {
    services.chunked(3).forEach { row ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            row.forEach { service ->
                ServiceCard(service, Modifier.weight(1f)) { onServiceClick(service) }
            }
            repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ServiceCard(service: ServiceItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(service.bgColor),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(service.iconRes),
                contentDescription = service.name,
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = service.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun BookingBanner(onFindHospital: () -> Unit, onSaved: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFFFCB216), Color(0xFFFFD76E))))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Reservasi Lebih Cepat", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF1A0A00))
            Spacer(Modifier.height(4.dp))
            Text("Pilih RS, jadwal, dan poli — semuanya dari satu tempat.", fontSize = 13.sp, color = Color(0xFF5A3A00), lineHeight = 18.sp)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onFindHospital,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A0A00)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Text("Cari RS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = onSaved,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1A0A00)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1A0A00)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Text("Tersimpan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}