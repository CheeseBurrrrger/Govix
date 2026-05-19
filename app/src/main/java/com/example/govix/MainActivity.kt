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
import com.example.govix.auth.ui.SignInScreen
import com.example.govix.dashboard.ui.HomeScreen
import com.example.govix.navigation.GovixNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        setContent {
//            GovixTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
            GovixNav()
        }
    }
}

@Composable
fun prototype(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.light_yellow_bg))
    ){
        Text("Gacor programmer handal berkelas tampan")
    }
}
