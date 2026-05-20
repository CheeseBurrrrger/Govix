package com.example.govix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.govix.auth.AuthViewModelFactory
import com.example.govix.auth.AuthViewModel
import com.example.govix.core.ui.theme.GovixTheme
import com.example.govix.navigation.GovixRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            GovixTheme {
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(application),
                )
                GovixRoot(authViewModel = authViewModel)
            }
        }
    }
}

@Composable
fun prototype() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.light_yellow_bg)),
    ) {
        Text("Gacor programmer handal berkelas tampan")
    }
}
