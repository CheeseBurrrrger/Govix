package com.example.govix.profile.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.govix.profile.domain.model.UpdateProfileRequest
import com.example.govix.profile.presentation.ProfileState
import com.example.govix.profile.presentation.ProfileViewModel
import com.example.govix.profile.presentation.UpdateProfileState

private val Yellow      = Color(0xFFFCB216)
private val YellowDeep  = Color(0xFFE09A00)
private val YellowLight = Color(0xFFFFD76E)
private val YellowPale  = Color(0xFFFFF8E7)
private val Surface     = Color(0xFFF7F7F7)
private val CardBg      = Color.White
private val TextPrimary = Color(0xFF1A1A1A)
private val TextHint    = Color(0xFF9E9E9E)
private val BorderColor = Color(0xFFE8E8E8)
private val ErrorRed    = Color(0xFFE53935)

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val updateState  by viewModel.updateState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var nik       by remember { mutableStateOf("") }
    var region    by remember { mutableStateOf("") }
    var address   by remember { mutableStateOf("") }
    var gender    by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val p = (profileState as ProfileState.Success).profile
            firstName = p.firstName.orEmpty()
            lastName  = p.lastName.orEmpty()
            phone     = p.phone.orEmpty()
            nik       = p.nik.orEmpty()
            region    = p.region.orEmpty()
            address   = p.address.orEmpty()
            gender    = p.gender.orEmpty()
            birthDate = p.birthDate.orEmpty()
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
            .background(Surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(YellowDeep, Yellow, YellowLight))
                )
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Kembali",
                    tint               = Color(0xFF1A0A00)
                )
            }
            Text(
                text       = "Edit Profil",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = Color(0xFF1A0A00),
                modifier   = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EditSection(title = "Identitas") {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(Modifier.weight(1f)) {
                        EditField(
                            label         = "Nama Depan",
                            value         = firstName,
                            icon          = Icons.Outlined.Person,
                            placeholder   = "Nama depan",
                            onValueChange = { firstName = it }
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        EditField(
                            label         = "Nama Belakang",
                            value         = lastName,
                            icon          = Icons.Outlined.Person,
                            placeholder   = "Nama belakang",
                            onValueChange = { lastName = it }
                        )
                    }
                }

                EditField(
                    label         = "NIK",
                    value         = nik,
                    icon          = Icons.Outlined.CreditCard,
                    placeholder   = "16 digit NIK",
                    keyboardType  = KeyboardType.Number,
                    isError       = nik.isNotBlank() && (nik.length != 16 || !nik.all { it.isDigit() }),
                    errorMessage  = "NIK harus 16 digit angka",
                    onValueChange = { if (it.length <= 16) nik = it }
                )

                // Gender dropdown
                EditDropdownField(
                    label    = "Jenis Kelamin",
                    icon     = Icons.Outlined.Wc,
                    value    = when (gender.uppercase()) {
                        "L" -> "Laki-laki"
                        "P" -> "Perempuan"
                        else -> gender
                    },
                    options  = listOf("Laki - Laki", "Perempuan"),
                    onSelect =
                        { selected ->
                            gender = when (selected) {
                                "Laki-laki" -> "L"
                                "Perempuan" -> "P"
                                else        -> selected
                            }
                        }
                )
                EditDateField(
                    label          = "Tanggal Lahir",
                    value          = birthDate,
                    onDateSelected = { birthDate = it }
                )
            }
            EditSection(title = "Kontak & Lokasi") {
                EditField(
                    label         = "No. HP",
                    value         = phone,
                    icon          = Icons.Outlined.Phone,
                    placeholder   = "08xxxxxxxxxx",
                    keyboardType  = KeyboardType.Phone,
                    onValueChange = { phone = it }
                )
                EditField(
                    label         = "Wilayah",
                    value         = region,
                    icon          = Icons.Outlined.LocationCity,
                    placeholder   = "Kota / Kabupaten",
                    onValueChange = { region = it }
                )
                EditField(
                    label         = "Alamat",
                    value         = address,
                    icon          = Icons.Outlined.Home,
                    placeholder   = "Alamat lengkap",
                    onValueChange = { address = it }
                )
            }
        }
        Surface(
            modifier      = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color         = CardBg
        ) {
            Button(
                onClick = {
                    viewModel.updateProfile(
                        UpdateProfileRequest(
                            firstName = firstName,
                            lastName  = lastName,
                            phone     = phone,
                            nik       = nik,
                            region    = region,
                            address   = address,
                            gender    = gender,
                            birthDate = birthDate,
                        )
                    )
                },
                enabled        = !isLoading,
                modifier       = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(54.dp),
                colors         = ButtonDefaults.buttonColors(
                    containerColor         = Yellow,
                    disabledContainerColor = Color(0xFFFDD06A)
                ),
                shape          = RoundedCornerShape(14.dp),
                elevation      = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.Save, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Simpan Perubahan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}
@Composable
private fun EditSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
        content()
    }
}
@Composable
private fun EditField(
    label: String,
    value: String,
    icon: ImageVector,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit,
) {
    Column {
        Text(label, fontSize = 12.sp, color = TextHint, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value           = value,
            onValueChange   = onValueChange,
            modifier        = Modifier.fillMaxWidth().height(54.dp),
            singleLine      = true,
            isError         = isError,
            placeholder     = { Text(placeholder, color = TextHint, fontSize = 14.sp) },
            leadingIcon     = {
                Icon(icon, null,
                    tint     = if (value.isNotBlank()) Yellow else TextHint,
                    modifier = Modifier.size(20.dp))
            },
            shape  = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Yellow,
                unfocusedBorderColor = BorderColor,
                errorBorderColor     = ErrorRed,
                focusedContainerColor   = YellowPale,
                unfocusedContainerColor = Color(0xFFFAFAFA),
            ),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        )
        if (isError && errorMessage != null) {
            Text(errorMessage, color = ErrorRed, fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDropdownField(
    label: String,
    icon: ImageVector,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, fontSize = 12.sp, color = TextHint, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(6.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value         = value.ifEmpty { "Pilih jenis kelamin" },
                onValueChange = {},
                readOnly      = true,
                leadingIcon   = {
                    Icon(icon, null,
                        tint     = if (value.isNotBlank()) Yellow else TextHint,
                        modifier = Modifier.size(20.dp))
                },
                trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier      = Modifier.fillMaxWidth().height(54.dp).menuAnchor(),
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = Yellow,
                    unfocusedBorderColor    = BorderColor,
                    focusedContainerColor   = YellowPale,
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text    = { Text(opt) },
                        onClick = { onSelect(opt); expanded = false }
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDateField(
    label: String,
    value: String,
    onDateSelected: (String) -> Unit,
) {
    var showDialog      by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    Column {
        Text(label, fontSize = 12.sp, color = TextHint, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFAFAFA))
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .clickable { showDialog = true },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier          = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.CalendarMonth, null,
                    tint     = if (value.isNotBlank()) Yellow else TextHint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text     = value.ifEmpty { "YYYY-MM-DD" },
                    fontSize = 14.sp,
                    color    = if (value.isNotBlank()) TextPrimary else TextHint,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Outlined.EditCalendar, "Pilih tanggal",
                        tint = TextHint, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton    = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        onDateSelected(sdf.format(java.util.Date(millis)))
                    }
                    showDialog = false
                }) { Text("Pilih", color = Yellow, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal", color = TextHint)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor           = Color.White,
                selectedDayContainerColor = Yellow,
                selectedDayContentColor  = Color.White,
                todayContentColor        = Yellow,
                todayDateBorderColor     = Yellow,
            )
        ) {
            DatePicker(state = datePickerState, showModeToggle = true)
        }
    }
}