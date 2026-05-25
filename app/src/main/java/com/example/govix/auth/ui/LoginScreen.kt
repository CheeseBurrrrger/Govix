package com.example.govix.auth.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.govix.R

private val GovixYellow      = Color(0xFFFCB216)
private val GovixYellowLight = Color(0xFFFDD06A)
private val PlaceholderGray  = Color(0xFF9E9E9E)
private val LabelGray        = Color(0xFF424242)
private val DividerGray      = Color(0xFFE0E0E0)
private val ErrorRed         = Color(0xFFE53935)

@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    onLogin: (String, String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val isFormValid = email.isNotBlank() && password.isNotBlank()

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
                .fillMaxHeight(0.80f)
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

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Selamat datang kembali 👋",
                fontSize = 15.sp,
                color = PlaceholderGray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Lupa Kata Sandi?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GovixYellow,
                    modifier = Modifier.clickable {  }
                )
            }

            Spacer(Modifier.height(20.dp))

            GovixPrimaryButton(
                text = "Masuk",
                isLoading = isLoading,
                enabled = isFormValid && !isLoading,
                onClick = {
                    if (isFormValid) {
                        onLogin(email.trim(), password)
                    } else {
                        Toast.makeText(context, "Isi semua kolom terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Belum punya akun?", fontSize = 14.sp, color = LabelGray)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Daftar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GovixYellow,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }

            Spacer(Modifier.height(20.dp))

            GovixDivider()

            Spacer(Modifier.height(20.dp))

            GovixSocialButton(
                iconRes = R.drawable.googleicon,
                text = "Masuk dengan Google",
                onClick = onGoogleClick
            )

            Spacer(Modifier.height(12.dp))

            GovixSocialButton(
                iconRes = R.drawable.fbicon,
                text = "Masuk dengan Facebook",
                onClick = onFacebookClick
            )

            Spacer(Modifier.height(28.dp))
        }

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


@Composable
fun GovixInputField(
    label: String,
    value: String,
    placeholder: String,
    leadingIcon: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onPasswordToggle: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = LabelGray,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = PlaceholderGray, fontSize = 14.sp) },
            singleLine = true,
            isError = isError,
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (value.isNotBlank()) GovixYellow else PlaceholderGray
                    )
                }
            },
            trailingIcon = if (isPassword && onPasswordToggle != null) {
                {
                    IconButton(onClick = onPasswordToggle) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.eye else R.drawable.hide
                            ),
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier.size(22.dp),
                            tint = PlaceholderGray
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GovixYellow,
                unfocusedBorderColor = DividerGray,
                errorBorderColor = ErrorRed,
                focusedContainerColor = Color(0xFFFFFDF5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp)
            )
        }
    }
}

@Composable
fun GovixPrimaryButton(
    text: String,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GovixYellow,
            disabledContainerColor = GovixYellowLight,
            contentColor = Color.White,
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun GovixSocialButton(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DividerGray),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF212121)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun GovixDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = DividerGray,
            thickness = 1.dp
        )
        Text(
            text = "  Atau  ",
            fontSize = 13.sp,
            color = PlaceholderGray
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = DividerGray,
            thickness = 1.dp
        )
    }
}