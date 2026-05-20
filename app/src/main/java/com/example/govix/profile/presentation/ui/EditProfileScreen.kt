package com.example.govix.profile.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.R
import com.example.govix.profile.domain.model.UpdateProfileRequest
import com.example.govix.profile.presentation.ProfileState
import com.example.govix.profile.presentation.ProfileViewModel
import com.example.govix.profile.presentation.UpdateProfileState

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val updateState  by viewModel.updateState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val profile = (profileState as? ProfileState.Success)?.profile

    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var nik       by remember { mutableStateOf("") }
    var region    by remember { mutableStateOf("") }
    var address   by remember { mutableStateOf("") }
    var gender    by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val p = (profileState as ProfileState.Success).profile
            firstName = p.firstName
            lastName  = p.lastName
            phone     = p.phone
            nik       = p.nik
            region    = p.region
            address   = p.address
            gender    = p.gender
            birthDate = p.birthDate
        }
    }
    LaunchedEffect(updateState) {
        when (updateState) {
            is UpdateProfileState.Success -> {
                Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateState()
                onBack()
            }
            is UpdateProfileState.Error -> {
                Toast.makeText(
                    context,
                    (updateState as UpdateProfileState.Error).message ?: "Gagal memperbarui profil",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetUpdateState()
            }
            else -> Unit
        }
    }

    val isLoading = updateState is UpdateProfileState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFCB216))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.arrowb), // use your back arrow drawable
                    contentDescription = "Kembali",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Edit Profil",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // ── Form ─────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileTextField(label = "Nama Depan",    value = firstName,  onValueChange = { firstName = it })
            ProfileTextField(label = "Nama Belakang", value = lastName,   onValueChange = { lastName = it })
            ProfileTextField(label = "No. HP",        value = phone,      onValueChange = { phone = it },
                keyboardType = KeyboardType.Phone)
            ProfileTextField(label = "NIK",           value = nik,        onValueChange = { nik = it },
                keyboardType = KeyboardType.Number)
            ProfileTextField(label = "Wilayah",       value = region,     onValueChange = { region = it })
            ProfileTextField(label = "Alamat",        value = address,    onValueChange = { address = it })
            ProfileTextField(label = "Jenis Kelamin", value = gender,     onValueChange = { gender = it })
            ProfileTextField(label = "Tanggal Lahir (YYYY-MM-DD)", value = birthDate, onValueChange = { birthDate = it })
        }

        Button(
            onClick = {
                viewModel.updateProfile(
                    UpdateProfileRequest(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        nik = nik,
                        region = region,
                        address = address,
                        gender = gender,
                        birthDate = birthDate,
                    )
                )
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFFCB216)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
            } else {
                Text(text = "Simpan Perubahan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF9E9E9E))
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}
