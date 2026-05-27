package com.example.govix.profile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.govix.R
import com.example.govix.profile.domain.model.Profile
import com.example.govix.profile.presentation.ProfileState
import com.example.govix.profile.presentation.ProfileViewModel
private val Yellow      = Color(0xFFFCB216)
private val YellowDeep  = Color(0xFFE09A00)
private val YellowLight = Color(0xFFFFD76E)
private val YellowPale  = Color(0xFFFFF8E7)
private val Surface     = Color(0xFFF7F7F7)
private val CardBg      = Color.White
private val TextPrimary = Color(0xFF1A1A1A)
private val TextHint    = Color(0xFF9E9E9E)
private val Divider     = Color(0xFFF0F0F0)

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditClick: () -> Unit,
) {
    val state by viewModel.profileState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.loadProfile() }

    when (val s = state) {
        is ProfileState.Loading, ProfileState.Idle -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Yellow)
            }
        }
        is ProfileState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(Icons.Outlined.ErrorOutline, null, tint = Color(0xFFE53935), modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(s.message ?: "Terjadi kesalahan", color = Color(0xFFE53935), fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadProfile() }, colors = ButtonDefaults.buttonColors(Yellow)) {
                        Text("Coba Lagi", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        is ProfileState.Success -> ProfileContent(profile = s.profile, onEditClick = onEditClick)
        else -> Unit
    }
}
@Composable
private fun ProfileContent(
    profile: Profile,
    onEditClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(YellowDeep, Yellow, YellowLight))
                )
                .padding(bottom = 48.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.avatarUrl != null) {
                        AsyncImage(
                            model              = profile.avatarUrl,
                            contentDescription = "Avatar",
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter            = painterResource(R.drawable.person),
                            contentDescription = null,
                            tint               = Yellow,
                            modifier           = Modifier.size(52.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                val displayName = when {
                    !profile.fullName.isNullOrBlank()  -> profile.fullName
                    !profile.firstName.isNullOrBlank() ->
                        listOfNotNull(profile.firstName, profile.lastName).joinToString(" ")
                    else -> profile.username
                }
                Text(
                    text       = displayName,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color(0xFF1A0A00)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = "@${profile.username}",
                    fontSize = 13.sp,
                    color    = Color(0xFF5A3A00)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = profile.email,
                    fontSize = 13.sp,
                    color    = Color(0xFF5A3A00)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileCard(title = "Identitas") {
                ProfileRow(Icons.Outlined.Person,       "Nama Depan",  profile.firstName.orDash())
                ProfileRow(Icons.Outlined.Person,       "Nama Belakang", profile.lastName.orDash())
                ProfileRow(Icons.Outlined.Badge,        "Username",    "@${profile.username}")
                ProfileRow(Icons.Outlined.CreditCard,   "NIK",         profile.nik.orDash())
                ProfileRow(Icons.Outlined.Wc,           "Jenis Kelamin",
                    when (profile.gender.trim().uppercase()) {
                        "L", "LAKI", "LAKI - LAKI" -> "Laki-laki"
                        "P", "PEREMPUAN"            -> "Perempuan"
                        else                        -> profile.gender.orDash()
                    }
                )
                ProfileRow(Icons.Outlined.Cake,         "Tanggal Lahir", profile.birthDate.orDash().take(10), isLast = true)
            }
            ProfileCard(title = "Kontak & Lokasi") {
                ProfileRow(Icons.Outlined.Email,        "Email",    profile.email)
                ProfileRow(Icons.Outlined.Phone,        "No. HP",   profile.phone.orDash())
                ProfileRow(Icons.Outlined.LocationCity, "Wilayah",  profile.region.orDash())
                ProfileRow(Icons.Outlined.Home,         "Alamat",   profile.address.orDash(), isLast = true)
            }
            Spacer(Modifier.height(4.dp))
            Button(
                onClick        = onEditClick,
                modifier       = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors         = ButtonDefaults.buttonColors(Yellow),
                shape          = RoundedCornerShape(14.dp),
                elevation      = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(Icons.Outlined.Edit, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Edit Profil", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}
@Composable
private fun ProfileCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(20.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
        Spacer(Modifier.height(16.dp))
        content()
    }
}
@Composable
private fun ProfileRow(
    icon: ImageVector,
    label: String,
    value: String,
    isLast: Boolean = false,
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(YellowPale),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Yellow, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, color = TextHint, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(2.dp))
            Text(value, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
    if (!isLast) {
        HorizontalDivider(color = Divider, thickness = 1.dp)
    }
}
private fun String?.orDash() = if (isNullOrBlank()) "—" else this