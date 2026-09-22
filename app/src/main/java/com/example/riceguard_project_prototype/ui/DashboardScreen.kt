package com.example.riceguard_project_prototype.ui

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riceguard_project_prototype.ui.theme.TextDarkPrimary
import com.example.riceguard_project_prototype.ui.theme.TextLightPrimary
import com.example.riceguard_project_prototype.ui.theme.TextLightSecondary
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var isDarkMode by rememberSaveable { mutableStateOf(false) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val hasAnyPermission = locationPermissionsState.allPermissionsGranted ||
            locationPermissionsState.revokedPermissions.size < locationPermissionsState.permissions.size

    LaunchedEffect(hasAnyPermission) {
        if (hasAnyPermission) {
            viewModel.fetchLiveLocationAndWeather(context)
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (!locationPermissionsState.allPermissionsGranted && locationPermissionsState.revokedPermissions.size == locationPermissionsState.permissions.size) {
            viewModel.fetchFallbackData(context, DashboardViewModel.DEFAULT_LOCATION_NAME, permissionGranted = false)
        }
    }

    // Animated color transitions for screen, header, and section title
    val screenBgColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF121212) else Color(0xFFF4F9F5),
        animationSpec = tween(durationMillis = 350),
        label = "ScreenBgAnim"
    )
    val topBarBgColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF0F3822) else Color(0xFF1D5C3A),
        animationSpec = tween(durationMillis = 350),
        label = "TopBarBgAnim"
    )
    val sectionTitleColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF81C784) else Color(0xFF1D5C3A),
        animationSpec = tween(durationMillis = 350),
        label = "SectionTitleAnim"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RiceGuard Dashboard", fontWeight = FontWeight.Bold, color = TextLightPrimary) },
                actions = {
                    IconButton(onClick = { isDarkMode = !isDarkMode }) {
                        AnimatedContent(
                            targetState = isDarkMode,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                            },
                            label = "ThemeIconAnimation"
                        ) { dark ->
                            Icon(
                                imageVector = if (dark) Icons.Default.WbSunny else Icons.Default.NightsStay,
                                contentDescription = if (dark) "Switch to Daytime Theme" else "Switch to Nighttime Theme",
                                tint = TextLightPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBgColor,
                    titleContentColor = TextLightPrimary,
                    actionIconContentColor = TextLightPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBgColor)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Permission Banner if denied
            if (!uiState.locationPermissionGranted) {
                item {
                    val bannerBg by animateColorAsState(
                        targetValue = if (isDarkMode) Color(0xFF3E2723) else Color(0xFFFFF3E0),
                        animationSpec = tween(durationMillis = 350),
                        label = "BannerBgAnim"
                    )
                    val bannerTextColor by animateColorAsState(
                        targetValue = if (isDarkMode) TextLightPrimary else TextDarkPrimary,
                        animationSpec = tween(durationMillis = 350),
                        label = "BannerTextAnim"
                    )
                    val bannerBtnColor by animateColorAsState(
                        targetValue = if (isDarkMode) Color(0xFF81C784) else Color(0xFF1D5C3A),
                        animationSpec = tween(durationMillis = 350),
                        label = "BannerBtnAnim"
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = bannerBg)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Permission Info",
                                    tint = Color(0xFFE65100)
                                )
                                Text(
                                    text = "Location access disabled. Showing weather for Davao City.",
                                    fontSize = 13.sp,
                                    color = bannerTextColor
                                )
                            }
                            TextButton(
                                onClick = { locationPermissionsState.launchMultiplePermissionRequest() }
                            ) {
                                Text("Enable", fontWeight = FontWeight.Bold, color = bannerBtnColor)
                            }
                        }
                    }
                }
            }

            // Weather Card
            item {
                WeatherCard(
                    weather = uiState.weather,
                    isLoading = uiState.isLoading,
                    isDarkMode = isDarkMode,
                    onRefreshClick = {
                        if (hasAnyPermission) {
                            viewModel.fetchLiveLocationAndWeather(context)
                        } else {
                            viewModel.fetchFallbackData(context, DashboardViewModel.DEFAULT_LOCATION_NAME, permissionGranted = false)
                        }
                    }
                )
            }

            // Section Header: Agricultural Advisories
            item {
                Text(
                    text = "Agricultural Advisories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = sectionTitleColor
                )
            }

            // Advisory Items
            items(uiState.advisories) { advisory ->
                AdvisoryItem(advisory = advisory, isDarkMode = isDarkMode)
            }
        }
    }
}

@Composable
fun WeatherCard(
    weather: DashboardWeatherData?,
    isLoading: Boolean,
    isDarkMode: Boolean,
    onRefreshClick: () -> Unit
) {
    val cardBgColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF164E2F) else Color(0xFF1D5C3A),
        animationSpec = tween(durationMillis = 350),
        label = "WeatherCardBgAnim"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Pin",
                        tint = Color(0xFF81C784),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = weather?.locationName ?: "Fetching Location...",
                        color = TextLightPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Weather",
                        tint = TextLightPrimary
                    )
                }
            }

            if (weather?.isFallback == true) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFFFFB74D), RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Default / Fallback Region",
                        color = Color(0xFFFFB74D),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else if (weather != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF81C784), RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Live GPS Location & Weather",
                        color = Color(0xFF81C784),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = TextLightPrimary,
                            strokeWidth = 2.5.dp
                        )
                        Text(
                            text = "Loading Live Forecast (Open-Meteo)...",
                            color = TextLightPrimary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherStat(
                        icon = Icons.Rounded.Thermostat,
                        value = "${weather?.temperature?.toInt() ?: "--"}°C",
                        label = "Temp"
                    )
                    WeatherStat(
                        icon = Icons.Rounded.WaterDrop,
                        value = "${weather?.humidity ?: "--"}%",
                        label = "Humidity"
                    )
                    WeatherStat(
                        icon = Icons.Rounded.Air,
                        value = "${String.format(Locale.US, "%.1f", weather?.windSpeed ?: 0.0)} m/s",
                        label = "Wind Speed"
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherStat(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Color(0xFF81C784), modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = TextLightPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = TextLightSecondary, fontSize = 12.sp)
    }
}

@Composable
fun AdvisoryItem(advisory: String, isDarkMode: Boolean) {
    val isWarning = advisory.contains("Warning") || advisory.contains("Unfavorable") || advisory.contains("⚠️")

    val targetCardBg = if (isDarkMode) {
        if (isWarning) Color(0xFF381E1E) else Color(0xFF1E2D22)
    } else {
        if (isWarning) Color(0xFFFFF5F5) else Color.White
    }

    val targetIconTint = if (isWarning) {
        if (isDarkMode) Color(0xFFFF8A80) else Color(0xFFD32F2F)
    } else {
        if (isDarkMode) Color(0xFF81C784) else Color(0xFF1D5C3A)
    }

    val targetTextColor = if (isDarkMode) TextLightPrimary else TextDarkPrimary

    val cardBg by animateColorAsState(
        targetValue = targetCardBg,
        animationSpec = tween(durationMillis = 350),
        label = "AdvisoryCardBgAnim"
    )
    val iconTint by animateColorAsState(
        targetValue = targetIconTint,
        animationSpec = tween(durationMillis = 350),
        label = "AdvisoryIconTintAnim"
    )
    val textColor by animateColorAsState(
        targetValue = targetTextColor,
        animationSpec = tween(durationMillis = 350),
        label = "AdvisoryTextColorAnim"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (isWarning) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = advisory,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
            )
        }
    }
}
