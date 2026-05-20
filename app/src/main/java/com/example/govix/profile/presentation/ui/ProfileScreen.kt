package com.example.govix.profile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                CircularProgressIndicator(color = Color(0xFFFCB216))
            }
        }
        is ProfileState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = s.message ?: "Terjadi kesalahan", color = Color(0xFFE53935))
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.loadProfile() },
                        colors = ButtonDefaults.buttonColors(Color(0xFFFCB216))
                    ) { Text("Coba Lagi") }
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
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFCB216))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBBDEFB)),
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.avatarUrl != null) {
                        AsyncImage(
                            model = profile.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.person),
                            contentDescription = null,
                            tint = Color(0xFF212121),
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = profile.fullName.ifBlank { profile.username },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = profile.email,
                    fontSize = 13.sp,
                    color = Color(0xFFBBDEFB)
                )
            }
        }

        // ── Info card ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Informasi Pribadi",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF212121)
            )
            Spacer(Modifier.height(8.dp))
            ProfileRow(label = "Username",    value = profile.username)
            ProfileRow(label = "NIK",         value = profile.nik.ifBlank { "-" })
            ProfileRow(label = "No. HP",      value = profile.phone.ifBlank { "-" })
            ProfileRow(label = "Jenis Kelamin", value = profile.gender.ifBlank { "-" })
            ProfileRow(label = "Tanggal Lahir", value = profile.birthDate.ifBlank { "-" })
            ProfileRow(label = "Wilayah",     value = profile.region.ifBlank { "-" })
            ProfileRow(label = "Alamat",      value = profile.address.ifBlank { "-" })
        }

        // ── Edit button ──────────────────────────────────────────────────────
        Button(
            onClick = onEditClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFFCB216)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Edit Profil",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Spacer(Modifier.height(80.dp))
    }
}


@Composable
private fun ProfileRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF9E9E9E))
        Text(text = value, fontSize = 15.sp, color = Color(0xFF212121))
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color(0xFFF5F5F5))
    }
}
