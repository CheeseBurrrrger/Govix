package com.example.govix.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.govix.auth.ui.LoginScreen
import com.example.govix.auth.ui.SignInScreen
import com.example.govix.dashboard.ui.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Signin

@Serializable
object Home

@Composable
fun GovixNav(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login, enterTransition = {
        fadeIn(
            animationSpec = tween(
                durationMillis = 450,
                easing = FastOutSlowInEasing
            )
        ) + slideInHorizontally(
            initialOffsetX = { (it * 0.10f).toInt() },
            animationSpec = tween(
                durationMillis = 450,
                easing = FastOutSlowInEasing
            )
        ) + scaleIn(
            initialScale = 0.985f,
            animationSpec = tween(
                durationMillis = 450,
                easing = FastOutSlowInEasing
            )
        )
    },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + slideOutHorizontally(
                targetOffsetX = { -(it * 0.06f).toInt() },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 450,
                    easing = FastOutSlowInEasing
                )
            ) + slideInHorizontally(
                initialOffsetX = { -(it * 0.10f).toInt() },
                animationSpec = tween(
                    durationMillis = 450,
                    easing = FastOutSlowInEasing
                )
            ) + scaleIn(
                initialScale = 0.985f,
                animationSpec = tween(
                    durationMillis = 450,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + slideOutHorizontally(
                targetOffsetX = { (it * 0.06f).toInt() },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        }
        ){
        composable <Login>{
            LoginScreen(navController)
        }
        composable <Signin>{
            SignInScreen(navController)
        }
        composable<Home> {
            HomeScreen(navController)
        }
    }
}