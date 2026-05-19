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

data class ServiceItem(
    val rank: Int,
    val name: String,
    val iconRes: Int
)

data class TabItem(val label: String)

private val tabs = listOf(
    TabItem("Layanan"),
)

private val favoriteServices = listOf(
    ServiceItem(1, "Rumah ASN",          R.drawable.rumahasn),
    ServiceItem(2, "Sapa Bansos", R.drawable.logo_aplikasi),
    ServiceItem(3, "Nomor Darurat",        R.drawable.logo_aplikasi),
    ServiceItem(4, "RSUD Dr.Soetomo",   R.drawable.logo_aplikasi),
    ServiceItem(5, "RSUD Saiful Anwar",   R.drawable.logo_aplikasi),
    ServiceItem(6, "Destinasi Wisata",   R.drawable.logo_aplikasi),
    ServiceItem(7, "Khas Jatim",   R.drawable.logo_aplikasi),
    ServiceItem(8, "RSUD karsa Husada",   R.drawable.logo_aplikasi),
    ServiceItem(9, "Sidita",   R.drawable.logo_aplikasi),
    ServiceItem(10, "RSUD Haji",   R.drawable.logo_aplikasi),





    )

// ---------------------------------------------------------------------------
// HomeScreen
// ---------------------------------------------------------------------------

@Composable
fun HomeScreen(
    navController: NavController,
    userName: String = "Pengunjung"
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {

        // ── Top header (blue) ──────────────────────────────────────────────
        TopHeader(userName = userName)

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
            ServiceGrid(services = favoriteServices)

            Spacer(modifier = Modifier.height(24.dp))

            // Banner image
            BannerSection()

            Spacer(modifier = Modifier.height(16.dp))

            // "Jawa Timur Dalam Angka" section
            StatisticsSection()

            Spacer(modifier = Modifier.height(80.dp)) // bottom nav clearance
        }
    }
}

// ---------------------------------------------------------------------------
// Top header
// ---------------------------------------------------------------------------

@Composable
private fun TopHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1565C0))
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
                    .background(Color(0xFFBBDEFB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.person), // swap with a person icon
                    contentDescription = "Avatar",
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Selamat pagi",
                    fontSize = 12.sp,
                    color = Color(0xFFBBDEFB)
                )
                Text(
                    text = userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Bell icon
        Icon(
            painter = painterResource(R.drawable.bell), // swap with bell icon drawable
            contentDescription = "Notifikasi",
            tint = Color.White,
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
        contentColor = Color(0xFF1565C0),
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = Color(0xFF1565C0)
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
                        color = if (selectedIndex == index) Color(0xFF1565C0) else Color(0xFF9E9E9E)
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
private fun ServiceGrid(services: List<ServiceItem>) {
    // Split into two rows: first row max 4 items, second row remainder
    val firstRow = services.take(4)
    val secondRow = services.drop(4)

    Column(modifier = Modifier.fillMaxWidth()) {
        ServiceRow(items = firstRow)
        if (secondRow.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            ServiceRow(items = secondRow)
        }
    }
}

@Composable
private fun ServiceRow(items: List<ServiceItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { service ->
            ServiceCard(service = service)
        }
    }
}

@Composable
private fun ServiceCard(service: ServiceItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
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
                    .background(Color(0xFF1565C0))
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
private fun BannerSection() {
    // Replace R.drawable.logo_aplikasi with your actual banner drawable
    Image(
        painter = painterResource(id = R.drawable.logo_aplikasi),
        contentDescription = "Banner",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}

// ---------------------------------------------------------------------------
// Statistics section
// ---------------------------------------------------------------------------

@Composable
private fun StatisticsSection() {
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
                tint = Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Jawa Timur Dalam Angka",
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
            items(listOf("Penduduk", "PDRB", "IPM")) { label ->
                StatCard(label = label)
            }
        }
    }
}

@Composable
private fun StatCard(label: String) {
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
            contentDescription = label,
            tint = Color(0xFF1565C0),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF616161),
            textAlign = TextAlign.Center
        )
    }
}