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
    val avgR: Int,
    val avgG: Int,
    val avgB: Int,
    val isFromGallery: Boolean = false
) : NavKey
