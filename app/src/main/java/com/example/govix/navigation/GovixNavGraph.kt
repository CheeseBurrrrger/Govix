package com.example.govix.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.govix.auth.AuthUiEvent
import com.example.govix.auth.AuthViewModel
import com.example.govix.auth.ui.HomeScreen
import com.example.govix.auth.ui.LoginScreen
import com.example.govix.auth.ui.SignInScreen

@Composable
fun GovixRoot(
    authViewModel: AuthViewModel,
) {
    val navController = rememberNavController()
    val hydrated by authViewModel.hydrated.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.events.collect { event ->
            when (event) {
                AuthUiEvent.NavigateHome -> {
                    navController.navigate(Screen.Home) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                is AuthUiEvent.NavigateToLogin -> {
                    event.message?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                    navController.navigate(Screen.Login) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                is AuthUiEvent.PlainToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    if (!hydrated) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (authViewModel.hasSession()) Screen.Home else Screen.Login
    GovixNavGraph(
        navController = navController,
        authViewModel = authViewModel,
        startDestination = startDestination,
    )
}

@Composable
fun GovixNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String,
) {
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Login) {
            LoginScreen(
                isLoading = isLoading,
                onLogin = { email, password -> authViewModel.login(email, password) },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register) { launchSingleTop = true }
                },
                onGoogleClick = { authViewModel.socialLoginPlaceholder() },
                onFacebookClick = { authViewModel.socialLoginPlaceholder() },
            )
        }
        composable(Screen.Register) {
            SignInScreen(
                isLoading = isLoading,
                onRegister = { first, last, user, phone, nik, addr, birth, gender, email, pass ->
                    authViewModel.register(first, last, user, phone, nik, addr, birth, gender, email, pass)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Register) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Screen.Home) {
            HomeScreen(
                onLogoutClick = { authViewModel.logout() },
            )
        }
    }
}
