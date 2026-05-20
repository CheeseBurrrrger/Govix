package com.example.govix.dashboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.govix.R
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.navigation.Screen

data class ServiceItem(
    val rank: Int,
    val name: String,
    val iconRes: Int,
    val isHospital: Boolean = false,
    val isEmergency: Boolean = false,
)

data class TabItem(val label: String)

private val tabs = listOf(
    TabItem("Kesehatan"),
)

private val favoriteServices = listOf(
    ServiceItem(1, "Nomor Darurat", R.drawable.logo_aplikasi, isEmergency = true),
    ServiceItem(2, "Daftar RS", R.drawable.layanan, isHospital = true),
    ServiceItem(3, "RSUD Dr.Soetomo", R.drawable.rsud_soetomo, isHospital = true),
    ServiceItem(4, "RSUD Saiful Anwar", R.drawable.rsud_saiful, isHospital = true),
    ServiceItem(5, "RSUD Karsa Husada", R.drawable.rsud_karsa, isHospital = true),
    ServiceItem(6, "RSUD Haji", R.drawable.rsud_haji, isHospital = true),
)

data class StatMetric(
    val label: String,
    val value: String,
    val caption: String,
)

private val dummyHealthStats = listOf(
    StatMetric("Antrean Aktif", "12", "hari ini"),
    StatMetric("Rata-rata Tunggu", "18", "menit"),
    StatMetric("Reservasi Bulan Ini", "143", "booking"),
    StatMetric("RS Terdekat", "5", "opsi"),
)

// ---------------------------------------------------------------------------
// Dashboard home (named to avoid clash with auth placeholder screens)
// ---------------------------------------------------------------------------

@Composable
fun DashboardHomeScreen(
    navController: NavController,
    hospitalViewModel: HospitalViewModel,
    userName: String = "Pengunjung",
    onLogoutClick: (() -> Unit)? = null,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {

        // ── Top header (blue) ──────────────────────────────────────────────
        TopHeader(userName = userName, onLogoutClick = onLogoutClick)

        // ── White card body ────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(top = 16.dp)
        ) {

            // Tab row
            ServiceTabRow(
                tabs = tabs,
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Service grid (LazyRow with wrapping via chunked rows)
            ServiceGrid(
                services = favoriteServices,
                onServiceClick = { service ->
                    when {
                        service.isEmergency -> navController.navigate(Screen.Emergency)
                        service.isHospital -> {
                            if (service.name == "Daftar RS") {
                                navController.navigate(Screen.HospitalList)
                            } else {
                                val id = hospitalViewModel.findHospitalIdByName(service.name)
                                if (id != null) navController.navigate(Screen.hospitalDetail(id))
                                else navController.navigate(Screen.HospitalList)
                            }
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Banner image
            BannerSection(
                onFindHospital = { navController.navigate(Screen.HospitalList) },
                onSaved = { navController.navigate(Screen.Saved) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "Jawa Timur Dalam Angka" section
            StatisticsSection(metrics = dummyHealthStats)

            Spacer(modifier = Modifier.height(80.dp)) // bottom nav clearance
        }
    }
}

// ---------------------------------------------------------------------------
// Top header
// ---------------------------------------------------------------------------

@Composable
private fun TopHeader(
    userName: String,
    onLogoutClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFCB216))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE29A))
                    .then(
                        if (onLogoutClick != null) {
                            Modifier.clickable { onLogoutClick() }
                        } else {
                            Modifier
                        },
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.personn), // swap with a person icon
                    contentDescription = "Avatar",
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Selamat pagi",
                    fontSize = 12.sp,
                    color = Color(0xFF212121)
                )
                Text(
                    text = userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }
        }

        // Bell icon
        Icon(
            painter = painterResource(R.drawable.bell), // swap with bell icon drawable
            contentDescription = "Notifikasi",
            tint = Color(0xFF212121),
            modifier = Modifier
                .size(28.dp)
                .clickable { /* navigate to notifications */ }
        )
    }
}

// ---------------------------------------------------------------------------
// Tab row
// ---------------------------------------------------------------------------

@Composable
private fun ServiceTabRow(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.White,
        contentColor = Color(0xFFFCB216),
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = Color(0xFFFCB216)
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = tab.label,
                        fontSize = 14.sp,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedIndex == index) Color(0xFFFCB216) else Color(0xFF9E9E9E)
                    )
                }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Service grid (2 rows, scrollable horizontally)
// ---------------------------------------------------------------------------

@Composable
private fun ServiceGrid(
    services: List<ServiceItem>,
    onServiceClick: (ServiceItem) -> Unit,
) {
    // Split into two rows: first row max 4 items, second row remainder
    val firstRow = services.take(4)
    val secondRow = services.drop(4)

    Column(modifier = Modifier.fillMaxWidth()) {
        ServiceRow(items = firstRow, onServiceClick = onServiceClick)
        if (secondRow.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            ServiceRow(items = secondRow, onServiceClick = onServiceClick)
        }
    }
}

@Composable
private fun ServiceRow(
    items: List<ServiceItem>,
    onServiceClick: (ServiceItem) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { service ->
            ServiceCard(service = service, onClick = { onServiceClick(service) })
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceItem,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable(onClick = onClick),
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = service.iconRes),
                    contentDescription = service.name,
                    modifier = Modifier.size(40.dp)
                )
            }
            // Rank badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFCB216))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "#${service.rank}",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = service.name,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            color = Color(0xFF212121)
        )
    }
}

// ---------------------------------------------------------------------------
// Banner
// ---------------------------------------------------------------------------

@Composable
private fun BannerSection(
    onFindHospital: () -> Unit,
    onSaved: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFFFF3D6))
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Reservasi rumah sakit lebih cepat",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF212121),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Cari rumah sakit, pilih poli & jadwal, lalu booking antrean dari akunmu.",
                fontSize = 12.sp,
                color = Color(0xFF616161),
            )
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onFindHospital,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCB216)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Cari RS", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onSaved,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Tersimpan", color = Color(0xFF212121), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Statistics section
// ---------------------------------------------------------------------------

@Composable
private fun StatisticsSection(metrics: List<StatMetric>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Trend icon — swap with your actual icon
            Icon(
                painter = painterResource(id = R.drawable.logo_aplikasi),
                contentDescription = null,
                tint = Color(0xFFFCB216),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ringkasan Kesehatan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Placeholder stat cards — replace with real data
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(metrics) { metric ->
                StatCard(metric = metric)
            }
        }
    }
}

@Composable
private fun StatCard(metric: StatMetric) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_aplikasi), // swap per category
            contentDescription = metric.label,
            tint = Color(0xFFFCB216),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = metric.value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            textAlign = TextAlign.Center,
        )
        Text(
            text = metric.label,
            fontSize = 12.sp,
            color = Color(0xFF616161),
            textAlign = TextAlign.Center,
        )
        Text(
            text = metric.caption,
            fontSize = 11.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center,
        )
    }
}
