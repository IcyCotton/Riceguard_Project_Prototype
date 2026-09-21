package com.example.riceguard_project_prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.riceguard_project_prototype.nav.*
import com.example.riceguard_project_prototype.ui.CameraScanScreen
import com.example.riceguard_project_prototype.ui.ClassificationResultScreen
import com.example.riceguard_project_prototype.ui.DashboardScreen
import com.example.riceguard_project_prototype.ui.ScanHomeScreen
import com.example.riceguard_project_prototype.ui.theme.Riceguard_Project_PrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Riceguard_Project_PrototypeTheme {
                val navigationState = rememberNavigationState(
                    startRoute = RouteScanHome,
                    topLevelRoutes = setOf(RouteScanHome, RouteDashboard)
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
                                onScanAnotherClick = { navigator.navigate(RouteScanHome) },
                                onBackClick = { navigator.goBack() }
                            )
                        }
                    }
                }

                NavDisplay(
                    entries = navigationState.toEntries(entryProvider),
                    onBack = { navigator.goBack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
