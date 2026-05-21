package com.example.govix.auth.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.govix.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ── Brand tokens (same as LoginScreen.kt) ───────────────────────
private val GovixYellow      = Color(0xFFFCB216)
private val GovixYellowLight = Color(0xFFFDD06A)
private val PlaceholderGray  = Color(0xFF9E9E9E)
private val LabelGray        = Color(0xFF424242)
private val DividerGray      = Color(0xFFE0E0E0)
private val ErrorRed         = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    isLoading: Boolean = false,
    onRegister: (
        firstName: String,
        lastName: String,
        username: String,
        phone: String,
        nik: String,
        region: String,
        address: String,
        birthDate: String,
        gender: String,
        email: String,
        password: String,
    ) -> Unit = { _, _, _, _, _, _, _, _, _, _, _ -> },
    onNavigateToLogin: () -> Unit = {},
) {
    // ── State ────────────────────────────────────────────────────
    var currentStep by remember { mutableStateOf(1) }

    // Step 1 fields
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var username  by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var nik       by remember { mutableStateOf("") }

    // Step 2 fields
    var address           by remember { mutableStateOf("") }
    var region            by remember { mutableStateOf("") }
    var birthDate         by remember { mutableStateOf("") }
    var gender            by remember { mutableStateOf("") }
    var genderExpanded    by remember { mutableStateOf(false) }
    val genderOptions     = listOf("Laki - Laki", "Perempuan")
    var email             by remember { mutableStateOf("") }
    var password          by rememberSaveable { mutableStateOf("") }
    var rePassword        by rememberSaveable { mutableStateOf("") }
    var passwordVisible   by rememberSaveable { mutableStateOf(false) }
    var rePasswordVisible by rememberSaveable { mutableStateOf(false) }
    var acceptTerms       by remember { mutableStateOf(false) }

    // ── Validation ───────────────────────────────────────────────
    val nikValid   = nik.length == 16 && nik.all { it.isDigit() }
    val step1Valid = firstName.isNotBlank() && lastName.isNotBlank() &&
            username.isNotBlank() && phone.isNotBlank() && nikValid

    val passwordsMatch = password == rePassword
    val step2Valid     = address.isNotBlank() && region.isNotBlank() &&
            birthDate.isNotBlank() && gender.isNotBlank() &&
            email.isNotBlank() && password.isNotBlank() &&
            rePassword.isNotBlank() && passwordsMatch &&
            acceptTerms && !isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.iseng),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(28.dp))

            // ── Logo ─────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_aplikasi),
                    contentDescription = "Logo Govix",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Govix",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 38.sp,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Step header ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (currentStep == 1) "Informasi Pribadi" else "Akun & Lokasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Langkah $currentStep dari 2",
                        fontSize = 13.sp,
                        color = PlaceholderGray
                    )
                }
                // Step bubbles
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(2) { index ->
                        val isActive  = index + 1 == currentStep
                        val isDone    = index + 1 < currentStep
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDone   -> GovixYellow
                                        isActive -> GovixYellow
                                        else     -> DividerGray
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isDone) "✓" else "${index + 1}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive || isDone) Color.White else PlaceholderGray
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Progress bar ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(2) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (index < currentStep) GovixYellow else DividerGray,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Step content ─────────────────────────────────────
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        (fadeIn() + slideInHorizontally { it }) togetherWith
                                (fadeOut() + slideOutHorizontally { -it })
                    } else {
                        (fadeIn() + slideInHorizontally { -it }) togetherWith
                                (fadeOut() + slideOutHorizontally { it })
                    }
                },
                label = "step_animation"
            ) { step ->
                if (step == 1) {
                    // ── STEP 1 ───────────────────────────────────
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                GovixInputField(
                                    label = "Nama Depan",
                                    value = firstName,
                                    placeholder = "Nama depan",
                                    onValueChange = { firstName = it }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                GovixInputField(
                                    label = "Nama Belakang",
                                    value = lastName,
                                    placeholder = "Nama belakang",
                                    onValueChange = { lastName = it }
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "Username",
                            value = username,
                            placeholder = "Nama pengguna unik",
                            onValueChange = { username = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "No. HP",
                            value = phone,
                            placeholder = "08xxxxxxxxxx",
                            keyboardType = KeyboardType.Phone,
                            onValueChange = { phone = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "NIK",
                            value = nik,
                            placeholder = "16 digit NIK",
                            keyboardType = KeyboardType.Number,
                            isError = nik.isNotBlank() && !nikValid,
                            errorMessage = "NIK harus 16 digit angka",
                            onValueChange = { if (it.length <= 16) nik = it }
                        )

                        Spacer(Modifier.height(28.dp))

                        GovixPrimaryButton(
                            text = "Selanjutnya →",
                            enabled = step1Valid && !isLoading,
                            onClick = { if (step1Valid) currentStep = 2 }
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sudah punya akun?", fontSize = 14.sp, color = LabelGray)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Masuk",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GovixYellow,
                                modifier = Modifier.clickable { onNavigateToLogin() }
                            )
                        }

                        Spacer(Modifier.height(28.dp))
                    }
                } else {
                    // ── STEP 2 ───────────────────────────────────
                    Column(modifier = Modifier.fillMaxWidth()) {

                        GovixInputField(
                            label = "Alamat",
                            value = address,
                            placeholder = "Alamat lengkap",
                            onValueChange = { address = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "Wilayah",
                            value = region,
                            placeholder = "Kota / Kabupaten",
                            onValueChange = { region = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        GovixDatePickerField(
                            label = "Tanggal Lahir",
                            value = birthDate,
                            onDateSelected = { birthDate = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        // ── Gender dropdown ───────────────────────
                        Text(
                            text = "Jenis Kelamin",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LabelGray,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        ExposedDropdownMenuBox(
                            expanded = genderExpanded,
                            onExpandedChange = { genderExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = gender.ifEmpty { "Pilih jenis kelamin" },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .menuAnchor(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GovixYellow,
                                    unfocusedBorderColor = DividerGray,
                                    focusedContainerColor = Color(0xFFFFFDF5),
                                    unfocusedContainerColor = Color(0xFFFAFAFA),
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false }
                            ) {
                                genderOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            gender = option
                                            genderExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "Email",
                            value = email,
                            placeholder = "nama@email.com",
                            leadingIcon = R.drawable.quill_mail,
                            keyboardType = KeyboardType.Email,
                            onValueChange = { email = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "Kata Sandi",
                            value = password,
                            placeholder = "Masukkan kata sandi",
                            leadingIcon = R.drawable.simple_line_icons_lock,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            passwordVisible = passwordVisible,
                            onPasswordToggle = { passwordVisible = !passwordVisible },
                            onValueChange = { password = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        GovixInputField(
                            label = "Konfirmasi Kata Sandi",
                            value = rePassword,
                            placeholder = "Ulangi kata sandi",
                            leadingIcon = R.drawable.simple_line_icons_lock,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            passwordVisible = rePasswordVisible,
                            isError = rePassword.isNotBlank() && !passwordsMatch,
                            errorMessage = "Kata sandi tidak cocok",
                            onPasswordToggle = { rePasswordVisible = !rePasswordVisible },
                            onValueChange = { rePassword = it }
                        )

                        Spacer(Modifier.height(14.dp))

                        // ── Terms checkbox ────────────────────────
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { acceptTerms = !acceptTerms }
                        ) {
                            Checkbox(
                                checked = acceptTerms,
                                onCheckedChange = { acceptTerms = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = GovixYellow,
                                    uncheckedColor = PlaceholderGray
                                )
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = LabelGray, fontSize = 13.sp)) {
                                        append("Saya menyetujui ")
                                    }
                                    withStyle(
                                        SpanStyle(
                                            color = GovixYellow,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    ) {
                                        append("Syarat & Kebijakan Privasi")
                                    }
                                }
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        GovixPrimaryButton(
                            text = "Daftar",
                            isLoading = isLoading,
                            enabled = step2Valid,
                            onClick = {
                                if (step2Valid) {
                                    onRegister(
                                        firstName, lastName, username, phone, nik,
                                        region, address, birthDate, gender, email, password
                                    )
                                }
                            }
                        )

                        Spacer(Modifier.height(12.dp))

                        // ── Back to step 1 ────────────────────────
                        TextButton(
                            onClick = { currentStep = 1 },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "← Kembali ke Langkah 1",
                                color = GovixYellow,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sudah punya akun?", fontSize = 14.sp, color = LabelGray)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Masuk",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GovixYellow,
                                modifier = Modifier.clickable { onNavigateToLogin() }
                            )
                        }

                        Spacer(Modifier.height(28.dp))
                    }
                }
            }
        }

        // ── Full-screen loading overlay ──────────────────────────
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = GovixYellow,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GovixDatePickerField(
    label: String,
    value: String,
    onDateSelected: (String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Text(
        text = label,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF424242),
        modifier = Modifier.padding(bottom = 6.dp)
    )

    OutlinedTextField(
        value = value.ifEmpty { "" },
        onValueChange = {},
        readOnly = true,
        placeholder = { Text("DD/MM/YYYY", color = Color(0xFF9E9E9E), fontSize = 14.sp) },
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Pilih tanggal",
                    tint = if (value.isNotBlank()) Color(0xFFFCB216) else Color(0xFF9E9E9E),
                    modifier = Modifier.size(22.dp)
                )
            }
        },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFCB216),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color(0xFFFFFDF5),
            unfocusedContainerColor = Color(0xFFFAFAFA),
            disabledBorderColor = Color(0xFFE0E0E0),
            disabledContainerColor = Color(0xFFFAFAFA),
            disabledTextColor = Color(0xFF212121),
        ),
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { showDialog = true }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(formatter.format(Date(millis)))
                    }
                    showDialog = false
                }) {
                    Text(
                        text = "Pilih",
                        color = Color(0xFFFCB216),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        text = "Batal",
                        color = Color(0xFF9E9E9E)
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF424242),
                headlineContentColor = Color(0xFFFCB216),
                weekdayContentColor = Color(0xFF9E9E9E),
                selectedDayContainerColor = Color(0xFFFCB216),
                selectedDayContentColor = Color.White,
                todayContentColor = Color(0xFFFCB216),
                todayDateBorderColor = Color(0xFFFCB216),
            )
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true,
            )
        }
    }
}