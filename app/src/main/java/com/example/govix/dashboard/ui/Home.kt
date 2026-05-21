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
import androidx.navigation.NavController
import com.example.govix.R
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.navigation.Screen
import java.util.Calendar

// ── Brand tokens ─────────────────────────────────────────────────────────────
private val Yellow        = Color(0xFFFCB216)
private val YellowLight   = Color(0xFFFDD06A)
private val YellowPale    = Color(0xFFFFF8E7)
private val YellowDeep    = Color(0xFFE09A00)
private val Surface       = Color(0xFFF7F7F7)
private val CardBg        = Color.White
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF6B6B6B)
private val TextHint      = Color(0xFFAAAAAA)

// ── Data models ──────────────────────────────────────────────────────────────

data class ServiceItem(
    val name: String,
    val iconRes: Int,
    val bgColor: Color = Color(0xFFFFF3D6),
    val isHospital: Boolean = false,
    val isEmergency: Boolean = false,
)

data class StatMetric(
    val label: String,
    val value: String,
    val caption: String,
    val iconRes: Int,
    val accentColor: Color = Yellow,
)

// ── Static data ───────────────────────────────────────────────────────────────

private val quickServices = listOf(
    ServiceItem("Darurat",    R.drawable.logo_aplikasi, Color(0xFFFFEBEB), isEmergency = true),
    ServiceItem("Daftar RS",  R.drawable.layanan,       Color(0xFFE8F4FF), isHospital  = true),
    ServiceItem("Dr.Soetomo", R.drawable.rsud_soetomo,  Color(0xFFE8FFE8), isHospital  = true),
    ServiceItem("Saiful A.",  R.drawable.rsud_saiful,   Color(0xFFFFF3D6), isHospital  = true),
    ServiceItem("Karsa H.",   R.drawable.rsud_karsa,    Color(0xFFF3E8FF), isHospital  = true),
    ServiceItem("RSUD Haji",  R.drawable.rsud_haji,     Color(0xFFE8F4FF), isHospital  = true),
)

private val healthStats = listOf(
    StatMetric("Antrean Aktif",       "12",  "hari ini",  R.drawable.logo_aplikasi, Color(0xFFFF6B6B)),
    StatMetric("Rata-rata Tunggu",    "18m", "per pasien",R.drawable.logo_aplikasi, Color(0xFF4ECDC4)),
    StatMetric("Reservasi Bulan Ini", "143", "booking",   R.drawable.logo_aplikasi, Yellow),
    StatMetric("RS Terdekat",         "5",   "pilihan",   R.drawable.logo_aplikasi, Color(0xFF6B8CFF)),
)

// ── Root screen ───────────────────────────────────────────────────────────────

@Composable
fun DashboardHomeScreen(
    navController: NavController,
    hospitalViewModel: HospitalViewModel,
    userName: String = "Pengunjung",
    onLogoutClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Hero header ───────────────────────────────────────────
        HeroHeader(userName = userName, onLogoutClick = onLogoutClick)

        // ── Content ───────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)           // overlap the header curve
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(top = 24.dp)
        ) {

            // Quick access
            SectionHeader(title = "Layanan Cepat", actionLabel = null)
            Spacer(Modifier.height(12.dp))
            ServiceGrid(
                services = quickServices,
                onServiceClick = { service ->
                    when {
                        service.isEmergency -> navController.navigate(Screen.Emergency)
                        service.isHospital  -> {
                            if (service.name == "Daftar RS") {
                                navController.navigate(Screen.HospitalList)
                            } else {
                                val id = hospitalViewModel.findHospitalIdByName(service.name)
                                if (id != null) navController.navigate(Screen.hospitalDetail(id))
                                else navController.navigate(Screen.HospitalList)
                            }
                        }
                    }
                }
            )

            Spacer(Modifier.height(28.dp))

            // Booking banner
            BookingBanner(
                onFindHospital = { navController.navigate(Screen.HospitalList) },
                onSaved        = { navController.navigate(Screen.Saved) },
            )

            Spacer(Modifier.height(28.dp))

            // Stats
            SectionHeader(
                title       = "Ringkasan Kesehatan",
                actionLabel = "Lihat semua",
                onAction    = {}
            )
            Spacer(Modifier.height(12.dp))
            StatsRow(metrics = healthStats)

            Spacer(Modifier.height(80.dp))   // bottom nav clearance
        }
    }
}

// ── Hero header ───────────────────────────────────────────────────────────────

@Composable
private fun HeroHeader(
    userName: String,
    onLogoutClick: (() -> Unit)?,
) {
    val hour     = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = when {
        hour < 11 -> "Selamat pagi"
        hour < 15 -> "Selamat siang"
        hour < 18 -> "Selamat sore"
        else      -> "Selamat malam"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(YellowDeep, Yellow, YellowLight)
                )
            )
            .padding(bottom = 36.dp)   // extra bottom so the content overlap looks clean
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier            = Modifier.fillMaxWidth(),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Avatar + greeting ─────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter           = painterResource(R.drawable.personn),
                        contentDescription = "Avatar",
                        tint              = Yellow,
                        modifier          = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text       = greeting,
                        fontSize   = 12.sp,
                        color      = Color(0xFF5A3A00),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text       = userName,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color(0xFF1A0A00),
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                }
            }

            // ── Logout button ─────────────────────────────────────
            if (onLogoutClick != null) {
                Column(
                    modifier              = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.12f))
                        .clickable { onLogoutClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment   = Alignment.CenterHorizontally,
                    verticalArrangement   = Arrangement.Center
                ) {
                    Icon(
                        imageVector       = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Logout",
                        tint              = Color(0xFF1A0A00),
                        modifier          = Modifier.size(20.dp)
                    )
                    Text(
                        text      = "Keluar",
                        fontSize  = 10.sp,
                        color     = Color(0xFF1A0A00),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String?,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = title,
            fontSize   = 16.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
        if (actionLabel != null && onAction != null) {
            Row(
                modifier          = Modifier.clickable { onAction() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text     = actionLabel,
                    fontSize = 13.sp,
                    color    = Yellow,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector       = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint              = Yellow,
                    modifier          = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ── Service grid ──────────────────────────────────────────────────────────────

@Composable
private fun ServiceGrid(
    services: List<ServiceItem>,
    onServiceClick: (ServiceItem) -> Unit,
) {
    val rows = services.chunked(3)
    Column(
        modifier            = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { service ->
                    ServiceCard(
                        service = service,
                        modifier = Modifier.weight(1f),
                        onClick  = { onServiceClick(service) }
                    )
                }
                // Fill empty slots so last row aligns left
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier            = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier         = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(service.bgColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter           = painterResource(id = service.iconRes),
                contentDescription = service.name,
                modifier          = Modifier.size(32.dp),
                contentScale      = ContentScale.Fit
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text       = service.name,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary,
            textAlign  = TextAlign.Center,
            lineHeight = 14.sp,
            maxLines   = 2,
            overflow   = TextOverflow.Ellipsis
        )
    }
}

// ── Booking banner ────────────────────────────────────────────────────────────

@Composable
private fun BookingBanner(
    onFindHospital: () -> Unit,
    onSaved: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFCB216), Color(0xFFFFD76E))
                )
            )
    ) {
        // Decorative circle accent
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = 240.dp, y = (-30).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = 280.dp, y = 40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .align(Alignment.TopStart)
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text       = "Reservasi Lebih Cepat",
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 18.sp,
                color      = Color(0xFF1A0A00)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text     = "Pilih RS, jadwal, dan poli — semuanya dari satu tempat.",
                fontSize = 13.sp,
                color    = Color(0xFF5A3A00),
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onFindHospital,
                    colors  = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A0A00),
                        contentColor   = Color.White
                    ),
                    shape             = RoundedCornerShape(12.dp),
                    contentPadding    = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Cari RS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = onSaved,
                    border  = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1A0A00)),
                    shape   = RoundedCornerShape(12.dp),
                    colors  = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1A0A00)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("Tersimpan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

// ── Stats row ─────────────────────────────────────────────────────────────────

@Composable
private fun StatsRow(metrics: List<StatMetric>) {
    LazyRow(
        contentPadding        = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(metrics) { metric ->
            StatCard(metric = metric)
        }
    }
}

@Composable
private fun StatCard(metric: StatMetric) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(metric.accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter           = painterResource(id = metric.iconRes),
                contentDescription = metric.label,
                tint              = metric.accentColor,
                modifier          = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text       = metric.value,
            fontSize   = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = TextPrimary
        )
        Text(
            text     = metric.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color    = TextPrimary,
            lineHeight = 16.sp
        )
        Text(
            text     = metric.caption,
            fontSize = 11.sp,
            color    = TextHint
        )
    }
}