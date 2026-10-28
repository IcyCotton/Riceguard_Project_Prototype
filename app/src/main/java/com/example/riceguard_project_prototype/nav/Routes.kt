package com.example.riceguard_project_prototype.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object RouteHome : NavKey

@Serializable
data object RouteHistory : NavKey

@Serializable
data object RouteScanHome : NavKey

@Serializable
data object RouteMap : NavKey

@Serializable
data object RouteShop : NavKey

@Serializable
data object RouteDashboard : NavKey

@Serializable
data object RouteCameraScan : NavKey

@Serializable
data class RouteResult(
    val avgR: Int = 0,
    val avgG: Int = 0,
    val avgB: Int = 0,
    val isFromGallery: Boolean = false,
    val imageUriString: String? = null
) : NavKey
