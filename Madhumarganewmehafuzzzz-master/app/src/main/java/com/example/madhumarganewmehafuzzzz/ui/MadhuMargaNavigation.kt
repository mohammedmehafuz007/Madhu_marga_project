package com.example.madhumarganewmehafuzzzz.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.madhumarganewmehafuzzzz.ui.auth.LoginScreen
import com.example.madhumarganewmehafuzzzz.ui.dashboard.DashboardScreen
import com.example.madhumarganewmehafuzzzz.ui.dashboard.DashboardViewModel
import com.example.madhumarganewmehafuzzzz.ui.analytics.AnalyticsScreen
import com.example.madhumarganewmehafuzzzz.ui.hive.AddEditHiveScreen
import com.example.madhumarganewmehafuzzzz.ui.hive.HiveListScreen
import com.example.madhumarganewmehafuzzzz.ui.hive.HiveViewModel
import com.example.madhumarganewmehafuzzzz.ui.inspection.AddInspectionScreen
import com.example.madhumarganewmehafuzzzz.ui.inspection.InspectionListScreen
import com.example.madhumarganewmehafuzzzz.ui.inspection.InspectionViewModel
import com.example.madhumarganewmehafuzzzz.ui.maps.HiveMapsScreen
import com.example.madhumarganewmehafuzzzz.ui.admin.AdminDashboardScreen
import com.example.madhumarganewmehafuzzzz.ui.chat.ChatScreen
import com.example.madhumarganewmehafuzzzz.ui.components.QrScannerScreen

@Composable
fun MadhuMargaNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("dashboard") {
            val dashboardViewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = dashboardViewModel,
                onManageHivesClick = {
                    navController.navigate("hive_list")
                },
                onViewMapsClick = {
                    navController.navigate("maps")
                },
                onViewAnalyticsClick = {
                    navController.navigate("analytics")
                },
                onChatClick = {
                    navController.navigate("chat")
                },
                onAdminClick = {
                    navController.navigate("admin_dashboard")
                }
            )
        }
        composable("chat") {
            ChatScreen(onBackClick = { navController.popBackStack() })
        }
        composable("admin_dashboard") {
            AdminDashboardScreen(
                onFarmerClick = { farmerId ->
                    // Navigate to farmer details if needed
                }
            )
        }
        composable("qr_scanner") {
            QrScannerScreen(
                onResult = { result ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("qr_result", result)
                    navController.popBackStack()
                },
                onClose = { navController.popBackStack() }
            )
        }
        composable("hive_list") {
            val viewModel: HiveViewModel = hiltViewModel()
            HiveListScreen(
                viewModel = viewModel,
                onAddHiveClick = { navController.navigate("add_edit_hive") },
                onHiveClick = { hive ->
                    navController.navigate("add_edit_hive?hiveId=${hive.id}")
                },
                onInspectClick = { hive ->
                    navController.navigate("inspection_list/${hive.id}")
                }
            )
        }
        composable(
            route = "add_edit_hive?hiveId={hiveId}",
            arguments = listOf(navArgument("hiveId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val viewModel: HiveViewModel = hiltViewModel()
            val hiveId = backStackEntry.arguments?.getString("hiveId")
            val qrResult = backStackEntry.savedStateHandle.get<String>("qr_result")
            
            AddEditHiveScreen(
                viewModel = viewModel,
                hiveId = hiveId,
                qrResult = qrResult,
                onScanQrClick = { navController.navigate("qr_scanner") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("inspection_list/{hiveId}") { backStackEntry ->
            val viewModel: InspectionViewModel = hiltViewModel()
            val hiveId = backStackEntry.arguments?.getString("hiveId") ?: ""
            InspectionListScreen(
                viewModel = viewModel,
                hiveId = hiveId,
                onAddInspectionClick = { navController.navigate("add_inspection/$hiveId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("add_inspection/{hiveId}") { backStackEntry ->
            val viewModel: InspectionViewModel = hiltViewModel()
            val hiveId = backStackEntry.arguments?.getString("hiveId") ?: ""
            AddInspectionScreen(
                viewModel = viewModel,
                hiveId = hiveId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("maps") {
            val viewModel: HiveViewModel = hiltViewModel()
            HiveMapsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("analytics") {
            AnalyticsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
