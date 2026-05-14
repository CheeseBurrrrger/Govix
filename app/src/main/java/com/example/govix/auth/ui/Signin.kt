package com.example.govix.auth.ui


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.govix.R

private val GovixYellow      = Color(0xFFFCB216)
private val GovixYellowLight = Color(0xFFFDD06A)
private val PlaceholderGray  = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen() {
    var currentStep by remember { mutableStateOf(1) }
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var nik       by remember { mutableStateOf("") }
    var address           by remember { mutableStateOf("") }
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
    val context = LocalContext.current
    val step1Valid = firstName.isNotBlank() && lastName.isNotBlank() &&
            phone.isNotBlank() && nik.isNotBlank()
    val step2Valid = address.isNotBlank() && birthDate.isNotBlank() &&
            gender.isNotBlank() && email.isNotBlank() &&
            password.isNotBlank() && rePassword.isNotBlank() &&
            password == rePassword && acceptTerms
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
                .fillMaxHeight(0.85f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                )
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_aplikasi),
                    contentDescription = "Logo aplikasi",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Govix",
                    fontWeight = FontWeight.Bold,
                    fontSize = 42.sp,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Daftar",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Langkah ", color = Color(0xFF616161), fontSize = 13.sp)
                Text(
                    text = "$currentStep",
                    color = GovixYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text("/2", color = Color(0xFF616161), fontSize = 13.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(2) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (index < currentStep) GovixYellow else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            AnimatedVisibility(
                visible = currentStep == 1,
                enter = fadeIn() + slideInHorizontally { -it },
                exit  = fadeOut() + slideOutHorizontally { -it }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    GovixField("Nama Depan",   firstName, "Nama Depan")   { firstName = it }
                    GovixField("Nama Belakang", lastName, "Nama Belakang") { lastName  = it }
                    GovixField("No HP",  phone, "08xxxxxxxxxx", KeyboardType.Phone)  { phone = it }
                    GovixField("NIK",    nik,   "16 digit NIK", KeyboardType.Number) { nik   = it }
                    Spacer(Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick  = { if (step1Valid) currentStep = 2 },
                            modifier = Modifier.size(width = 327.dp, height = 56.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor         = GovixYellow,
                                disabledContainerColor = GovixYellowLight
                            ),
                            shape   = RoundedCornerShape(12.dp),
                            enabled = step1Valid
                        ) {
                            Text(
                                text       = "Selanjutnya",
                                fontWeight = FontWeight.Bold,
                                fontSize   = 20.sp
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Sudah mempunyai akun?", fontWeight = FontWeight.Normal)
                            Text(
                                text       = " Masuk",
                                fontWeight = FontWeight.Bold,
                                color      = GovixYellow,
                                modifier   = Modifier.clickable { /* navController.navigate(login) */ }
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
            AnimatedVisibility(
                visible = currentStep == 2,
                enter = fadeIn() + slideInHorizontally { it },
                exit  = fadeOut() + slideOutHorizontally { it }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    GovixField("Alamat", address, "Alamat lengkap") { address = it }
                    GovixField("Tanggal Lahir", birthDate, "DD/MM/YYYY", KeyboardType.Number) {
                        birthDate = it
                    }
                    Text(
                        text     = "Jenis Kelamin",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded         = genderExpanded,
                        onExpandedChange = { genderExpanded = it }
                    ) {
                        OutlinedTextField(
                            value         = gender.ifEmpty { "Pilih jenis kelamin" },
                            onValueChange = {},
                            readOnly      = true,
                            trailingIcon  = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(horizontal = 0.dp, vertical = 6.dp)
                                .size(width = 327.dp, height = 56.dp)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp)),
                            shape  = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor   = Color.Transparent
                            )
                        )
                        ExposedDropdownMenu(
                            expanded         = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text    = { Text(option) },
                                    onClick = {
                                        gender         = option
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Text(
                        text     = "Email",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        placeholder   = { Text("E-Mail", color = PlaceholderGray) },
                        leadingIcon   = {
                            Icon(
                                painter            = painterResource(R.drawable.quill_mail),
                                contentDescription = "Email Icon",
                                modifier           = Modifier.size(22.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine      = true,
                        modifier        = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp, vertical = 6.dp)
                            .size(width = 327.dp, height = 56.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Text(
                        text     = "Kata Sandi",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it },
                        placeholder   = { Text("Kata Sandi", color = PlaceholderGray) },
                        leadingIcon   = {
                            Icon(
                                painter            = painterResource(R.drawable.simple_line_icons_lock),
                                contentDescription = "Lock Icon",
                                modifier           = Modifier.size(22.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        if (passwordVisible) R.drawable.eye else R.drawable.hide
                                    ),
                                    contentDescription = "Toggle password",
                                    modifier           = Modifier.size(24.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier   = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .size(width = 327.dp, height = 56.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Text(
                        text     = "Konfirmasi Kata Sandi",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    OutlinedTextField(
                        value         = rePassword,
                        onValueChange = { rePassword = it },
                        placeholder   = { Text("Konfirmasi Kata Sandi", color = PlaceholderGray) },
                        leadingIcon   = {
                            Icon(
                                painter            = painterResource(R.drawable.simple_line_icons_lock),
                                contentDescription = "Lock Icon",
                                modifier           = Modifier.size(22.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { rePasswordVisible = !rePasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        if (rePasswordVisible) R.drawable.eye else R.drawable.hide
                                    ),
                                    contentDescription = "Toggle re-password",
                                    modifier           = Modifier.size(24.dp)
                                )
                            }
                        },
                        visualTransformation = if (rePasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        isError    = rePassword.isNotBlank() && password != rePassword,
                        singleLine = true,
                        modifier   = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .size(width = 327.dp, height = 56.dp)
                            .border(
                                width = 1.dp,
                                color = if (rePassword.isNotBlank() && password != rePassword)
                                    Color(0xFFE53935) else Color.LightGray,
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    if (rePassword.isNotBlank() && password != rePassword) {
                        Text(
                            text     = "Kata sandi tidak cocok",
                            color    = Color(0xFFE53935),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier          = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked         = acceptTerms,
                            onCheckedChange = { acceptTerms = it },
                            colors          = CheckboxDefaults.colors(checkedColor = GovixYellow)
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color.Black, fontSize = 13.sp)) {
                                    append("Saya menyetujui ")
                                }
                                withStyle(
                                    SpanStyle(
                                        color      = GovixYellow,
                                        fontWeight = FontWeight.Bold,
                                        fontSize   = 13.sp
                                    )
                                ) {
                                    append("Syarat & Kebijakan Privasi")
                                }
                            },
                            modifier = Modifier.clickable { acceptTerms = !acceptTerms }
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                if (step2Valid) {
                                    Toast.makeText(context, "Mendaftar...", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(width = 327.dp, height = 56.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor         = GovixYellow,
                                disabledContainerColor = GovixYellowLight
                            ),
                            shape   = RoundedCornerShape(12.dp),
                            enabled = step2Valid
                        ) {
                            Text(
                                text       = "Daftar",
                                fontWeight = FontWeight.Bold,
                                fontSize   = 20.sp
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text       = "← Kembali ke langkah 1",
                            color      = GovixYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 14.sp,
                            modifier   = Modifier.clickable { currentStep = 1 }
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Sudah mempunyai akun?", fontWeight = FontWeight.Normal)
                            Text(
                                text       = " Masuk",
                                fontWeight = FontWeight.Bold,
                                color      = GovixYellow,
                                modifier   = Modifier.clickable { /* navController.navigate(login) */ }
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun GovixField(
    label         : String,
    value         : String,
    placeholder   : String,
    keyboardType  : KeyboardType = KeyboardType.Text,
    onValueChange : (String) -> Unit
) {
    Text(
        text     = label,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        placeholder     = { Text(placeholder, color = Color(0xFF757575)) },
        singleLine      = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier        = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 6.dp)
            .size(width = 327.dp, height = 56.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp)
    )
}