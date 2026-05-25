package com.example.govix.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.govix.R
private val NavYellow      = Color(0xFFFCB216)
private val NavUnselected  = Color(0xFF9E9E9E)
private val NavBackground  = Color.White

sealed class BottomNavRoute(val route: String) {
    object Beranda    : BottomNavRoute("beranda")
    object Tersimpan  : BottomNavRoute("tersimpan")
    object Akun       : BottomNavRoute("akun")
}
data class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int,
)
private val navItems = listOf(
    BottomNavItem(BottomNavRoute.Beranda.route,   "Beranda",   R.drawable.home),

    BottomNavItem(BottomNavRoute.Tersimpan.route, "Tersimpan", R.drawable.saved),
    BottomNavItem(BottomNavRoute.Akun.route,      "Akun",      R.drawable.personn),
)
@Composable
fun GovixBottomNavBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
) {
    NavigationBar(
        containerColor = NavBackground,
        tonalElevation = 8.dp,
        modifier = Modifier.height(64.dp)
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = NavYellow,
                    selectedTextColor   = NavYellow,
                    unselectedIconColor = NavUnselected,
                    unselectedTextColor = NavUnselected,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}