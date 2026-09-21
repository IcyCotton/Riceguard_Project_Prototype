package com.example.riceguard_project_prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.riceguard_project_prototype.nav.*
import com.example.riceguard_project_prototype.ui.*
import com.example.riceguard_project_prototype.ui.theme.Riceguard_Project_PrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Riceguard_Project_PrototypeTheme {
                val navigationState = rememberNavigationState(
                    startRoute = RouteScanHome,
                    topLevelRoutes = setOf(RouteHome, RouteHistory, RouteScanHome, RouteMap, RouteShop)
                )
                val navigator = remember { Navigator(navigationState) }

                val entryProvider = remember {
                    entryProvider {
                        entry<RouteScanHome> {
                            ScanHomeScreen(
                                onTakeExternalPhotoClick = { navigator.navigate(RouteCameraScan) },
                                onUploadFromGalleryClick = { r, g, b ->
                                    navigator.navigate(RouteResult(r, g, b, isFromGallery = true))
                                },
                                onDashboardClick = { navigator.navigate(RouteDashboard) }
                            )
                        }
                        entry<RouteDashboard> {
                            DashboardScreen(
                                onBackClick = { navigator.goBack() }
                            )
                        }
                        entry<RouteHome> {
                            DashboardScreen(
                                onBackClick = { navigator.goBack() }
                            )
                        }
                        entry<RouteHistory> {
                            HistoryScreen()
                        }
                        entry<RouteMap> {
                            MapScreen()
                        }
                        entry<RouteShop> {
                            ShopScreen()
                        }
                        entry<RouteCameraScan> {
                            CameraScanScreen(
                                onBackClick = { navigator.goBack() },
                                onImageCaptured = { r, g, b ->
                                    navigator.navigate(RouteResult(r, g, b, isFromGallery = false))
                                },
                                onGalleryClick = {
                                    navigator.navigate(RouteResult(130, 80, 40, isFromGallery = true))
                                }
                            )
                        }
                        entry<RouteResult> { key ->
                            ClassificationResultScreen(
                                avgR = key.avgR,
                                avgG = key.avgG,
                                avgB = key.avgB,
                                isFromGallery = key.isFromGallery,
                                onScanAnotherClick = {
                                    val currentStack = navigator.state.backStacks[navigator.state.topLevelRoute]
                                    if (currentStack != null && currentStack.contains(RouteCameraScan)) {
                                        while (currentStack.last() != RouteCameraScan) {
                                            currentStack.removeLastOrNull()
                                        }
                                    } else {
                                        if (currentStack != null && currentStack.lastOrNull() is RouteResult) {
                                            currentStack.removeLastOrNull()
                                        }
                                        navigator.navigate(RouteCameraScan)
                                    }
                                },
                                onBackClick = { navigator.goBack() }
                            )
                        }
                    }
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentRoute = navigationState.topLevelRoute

                            // 1. Home
                            NavigationBarItem(
                                selected = currentRoute == RouteHome,
                                onClick = { navigator.navigate(RouteHome) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") }
                            )
                            // 2. History
                            NavigationBarItem(
                                selected = currentRoute == RouteHistory,
                                onClick = { navigator.navigate(RouteHistory) },
                                icon = { Icon(Icons.Default.History, contentDescription = "History") },
                                label = { Text("History") }
                            )
                            // 3. Scan
                            NavigationBarItem(
                                selected = currentRoute == RouteScanHome,
                                onClick = { navigator.navigate(RouteScanHome) },
                                icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Scan") },
                                label = { Text("Scan") }
                            )
                            // 4. Map
                            NavigationBarItem(
                                selected = currentRoute == RouteMap,
                                onClick = { navigator.navigate(RouteMap) },
                                icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                                label = { Text("Map") }
                            )
                            // 5. Shop
                            NavigationBarItem(
                                selected = currentRoute == RouteShop,
                                onClick = { navigator.navigate(RouteShop) },
                                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Shop") },
                                label = { Text("Shop") }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavDisplay(
                        entries = navigationState.toEntries(entryProvider),
                        onBack = { navigator.goBack() },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}
