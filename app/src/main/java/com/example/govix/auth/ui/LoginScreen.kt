package com.example.govix.auth.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.govix.R

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.iseng),
                contentScale = ContentScale.Crop
            )
    ) {
        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
        var RePasswordVisibility by rememberSaveable { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        val icon = if (passwordVisibility)
            painterResource(id = R.drawable.eye)
        else
            painterResource(id = R.drawable.hide)
        val icon2 = if (RePasswordVisibility)
            painterResource(id = R.drawable.eye)
        else
            painterResource(id = R.drawable.hide)

        val ValidCheck =
            email.isNotBlank()
                    && password.isNotBlank()

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                )
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter), Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()

            ) {
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Govix",
//                        fontFamily = SFProdisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 42.sp,
                        color = Color.Black
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Email",
                    fontSize = 16.sp,
//            fontFamily = SFProdisplayFontFamily
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = {
                        Text(
                            text = "E-Mail",
                            color = Color(0xFF757575),
//                    fontFamily = SFProdisplayFontFamily
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.quill_mail),
                            contentDescription = "Email Icon",
                            modifier = Modifier.size(width = 22.dp, height = 22.dp),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .size(width = 327.dp, height = 56.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    singleLine = true,

                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Kata Sandi",
                    fontSize = 16.sp,
//            fontFamily = SFProdisplayFontFamily
                )
                //Password container
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = "Kata Sandi",
                            color = Color(0xFF757575),
//                    fontFamily = SFProdisplayFontFamily
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Icon(
                                painter = icon,
                                contentDescription = "Visibility Icon",
                                modifier = Modifier.size(width = 24.dp, height = 24.dp)
                            )
                        }
                    },

                    visualTransformation = if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.simple_line_icons_lock),
                            contentDescription = "Lock Icon",
                            modifier = Modifier.size(width = 22.dp, height = 22.dp),
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .size(width = 327.dp, height = 56.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(24.dp)
                        ),

                    shape = RoundedCornerShape(24.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {

                        Text(
                            text = "Lupa Kata Sandi ?",
                            fontSize = 14.sp,
//                            fontFamily = SFProdisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDA990E),
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 12.dp)
                                .clickable {
//                                    navController.navigate(resetpassword)

                                },
                            textAlign = TextAlign.End

                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Button(
                            onClick = {
                                if (email.isNotEmpty() && password.isNotEmpty()) {
//                        viewModel.viewModelScope.launch {
//                            val result = Email.signup(email, password)
//                            viewModel.onSignUpResult(result)
//                            Toast.makeText(
//                                context,
//                                "Signed Up!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please fill all fields",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .size(width = 327.dp, height = 56.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFFCB216)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = ValidCheck
                        )
                        {
                            Text(
                                text = "Masuk",
//                    fontFamily = SFProdisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Belum mempunyai akun?",
//                    fontFamily = SFProdisplayFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                            Text(
                                text = " Daftar",
//                    fontFamily = SFProdisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFCB216),
                                modifier = Modifier.clickable {
//                        navController.navigate(sign_in)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.padding(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.line),
                                contentDescription = "line",
                                modifier = Modifier.padding(8.dp),

                                )

                            Text(
                                text = "Atau",
//                fontFamily = SFProdisplayFontFamily,
                                fontSize = 14.sp
                            )

                            Image(
                                painter = painterResource(id = R.drawable.line),
                                contentDescription = "line",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {}
//                    onSignInClick
                                ,
                                modifier = Modifier
                                    .size(width = 327.dp, height = 56.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFCB216),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
//                            shape = RoundedCornerShape(24.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.googleicon),
                                        contentDescription = "Logo Google",
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Masuk dengan Google",
//                        fontFamily = SFProdisplayFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(7.dp))

                        Button(
                            onClick = {}
//                    onSignInClick
                            ,
                            modifier = Modifier
                                .size(width = 327.dp, height = 56.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFFCB216),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
//                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.fbicon),
                                    contentDescription = "Logo Google",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Masuk dengan Facebook",
//                        fontFamily = SFProdisplayFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(7.dp))
            }
        }
    }
}