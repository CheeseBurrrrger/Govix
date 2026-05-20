//package com.example.govix.navigation
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.govix.auth.AuthUiEvent
//import com.example.govix.auth.AuthViewModel
//import com.example.govix.auth.ui.LoginScreen
//import com.example.govix.auth.ui.SignInScreen
//import com.example.govix.dashboard.ui.DashboardHomeScreen
//import com.example.govix.hospital.HospitalViewModel
//import com.example.govix.hospital.HospitalViewModelFactory
//import com.example.govix.hospital.ui.EmergencyScreen
//import com.example.govix.hospital.ui.HospitalDetailScreen
//import com.example.govix.hospital.ui.HospitalListScreen
//import com.example.govix.hospital.ui.HospitalQueueScreen
//import com.example.govix.hospital.ui.HospitalRoomsScreen
//
//
//
//@Composable
//fun GovixNavGraph(
//    navController: NavHostController,
//    authViewModel: AuthViewModel,
//    startDestination: String,
//) {
//    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//    val hospitalViewModel: HospitalViewModel = viewModel(
//        factory = HospitalViewModelFactory(context.applicationContext as android.app.Application),
//    )
//
//
//}
package com.example.govix.navigation
import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.govix.auth.AuthUiEvent
import com.example.govix.auth.AuthViewModel
import com.example.govix.auth.ui.LoginScreen
import com.example.govix.auth.ui.SignInScreen
import com.example.govix.dashboard.ui.DashboardHomeScreen
import com.example.govix.hospital.HospitalViewModel
import com.example.govix.hospital.HospitalViewModelFactory
import com.example.govix.hospital.ui.EmergencyScreen
import com.example.govix.hospital.ui.HospitalDetailScreen
import com.example.govix.hospital.ui.HospitalListScreen
import com.example.govix.hospital.ui.HospitalQueueScreen
import com.example.govix.hospital.ui.HospitalRoomsScreen
import com.example.govix.profile.presentation.ProfileViewModel
import com.example.govix.profile.presentation.ui.EditProfileScreen
import com.example.govix.profile.presentation.ui.ProfileScreen


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

                AuthUiEvent.NavigateHome -> TODO()
                is AuthUiEvent.NavigateToLogin -> TODO()
                is AuthUiEvent.PlainToast -> TODO()
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
    val context = LocalContext.current
    val hospitalViewModel: HospitalViewModel = viewModel(
        factory = HospitalViewModelFactory(context.applicationContext as Application),
    )

    val bottomNavRoutes = setOf(
        Screen.Home,
        Screen.HospitalList,
        Screen.Profile,

        // add Tersimpan / Akun routes here when ready
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                GovixBottomNavBar(
                    currentRoute = when (currentRoute) {
                        Screen.Home         -> BottomNavRoute.Beranda.route
                        Screen.HospitalList -> BottomNavRoute.Layanan.route
                        else                -> BottomNavRoute.Beranda.route
                    },
                    onItemClick = { route ->
                        when (route) {
                            BottomNavRoute.Beranda.route   -> navController.navigate(Screen.Home) {
                                popUpTo(Screen.Home) { inclusive = false }
                                launchSingleTop = true
                            }
                            BottomNavRoute.Layanan.route   -> navController.navigate(Screen.HospitalList) {
                                launchSingleTop = true
                            }
                            BottomNavRoute.Tersimpan.route -> { /* TODO: navigate to Tersimpan */ }
                            BottomNavRoute.Akun.route -> navController.navigate(Screen.Profile) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
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
            LaunchedEffect(Unit) {
                hospitalViewModel.loadHospitals()
            }
            DashboardHomeScreen(
                navController = navController,
                hospitalViewModel = hospitalViewModel,
                onLogoutClick = { authViewModel.logout() },
            )
        }
        composable(Screen.HospitalList) {
            HospitalListScreen(
                viewModel = hospitalViewModel,
                onHospitalClick = { id, _ ->
                    navController.navigate(Screen.hospitalDetail(id))
                },
                onEmergencyClick = { navController.navigate(Screen.Emergency) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Screen.HospitalDetail,
            arguments = listOf(navArgument("hospitalId") { type = NavType.IntType }),
        ) { entry ->
            val hospitalId = entry.arguments?.getInt("hospitalId") ?: return@composable
            HospitalDetailScreen(
                hospitalId = hospitalId,
                viewModel = hospitalViewModel,
                onBack = { navController.popBackStack() },
                onQueueClick = { id, name ->
                    navController.navigate(Screen.hospitalQueue(id))
                },
                onRoomsClick = { id ->
                    navController.navigate(Screen.hospitalRooms(id))
                },
            )
        }
        composable(
            route = Screen.HospitalRooms,
            arguments = listOf(navArgument("hospitalId") { type = NavType.IntType }),
        ) { entry ->
            val hospitalId = entry.arguments?.getInt("hospitalId") ?: return@composable
            HospitalRoomsScreen(
                hospitalId = hospitalId,
                viewModel = hospitalViewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Screen.HospitalQueue,
            arguments = listOf(navArgument("hospitalId") { type = NavType.IntType }),
        ) { entry ->
            val hospitalId = entry.arguments?.getInt("hospitalId") ?: return@composable
            val hospitalName = hospitalViewModel.detailState.value.hospital?.name
                ?: hospitalViewModel.listState.value.hospitals
                    .firstOrNull { it.id == hospitalId }
                    ?.name
                ?: "Rumah Sakit"
            HospitalQueueScreen(
                hospitalId = hospitalId,
                hospitalName = hospitalName.orEmpty(),
                viewModel = hospitalViewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Emergency) {
            EmergencyScreen(onBack = { navController.popBackStack() })
        }
            composable(Screen.Profile) {
                ProfileScreen(
                    onEditClick = { navController.navigate(Screen.EditProfile) }
                )
            }
            composable(Screen.EditProfile) {
                val profileEntry = remember(it) {
                    navController.getBackStackEntry(Screen.Profile)
                }
                val viewModel: ProfileViewModel = hiltViewModel(profileEntry)
                EditProfileScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
    }
    }
}

