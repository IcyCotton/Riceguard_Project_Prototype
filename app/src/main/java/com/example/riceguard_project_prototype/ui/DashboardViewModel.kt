package com.example.riceguard_project_prototype.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riceguard_project_prototype.data.AdvisoryService
import com.example.riceguard_project_prototype.data.network.NetworkClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

data class DashboardWeatherData(
    val locationName: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val isFallback: Boolean = false
)

data class DashboardUiState(
    val isLoading: Boolean = false,
    val weather: DashboardWeatherData? = null,
    val advisories: List<String> = emptyList(),
    val error: String? = null,
    val locationPermissionGranted: Boolean = true
)

class DashboardViewModel(
    private val advisoryService: AdvisoryService = AdvisoryService()
) : ViewModel() {

    companion object {
        const val DEFAULT_LAT = 7.0736
        const val DEFAULT_LON = 125.6110
        const val DEFAULT_LOCATION_NAME = "Davao City"
    }

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    @SuppressLint("MissingPermission")
    fun fetchLiveLocationAndWeather(context: Context) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, locationPermissionGranted = true)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                fetchWeatherForCoordinates(context, location.latitude, location.longitude)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                    if (lastLoc != null) {
                        fetchWeatherForCoordinates(context, lastLoc.latitude, lastLoc.longitude)
                    } else {
                        fetchFallbackData(context, DEFAULT_LOCATION_NAME)
                    }
                }.addOnFailureListener {
                    fetchFallbackData(context, DEFAULT_LOCATION_NAME)
                }
            }
        }.addOnFailureListener {
            fetchFallbackData(context, DEFAULT_LOCATION_NAME)
        }
    }

    fun fetchFallbackData(
        context: Context,
        locationName: String = DEFAULT_LOCATION_NAME,
        permissionGranted: Boolean = true
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, locationPermissionGranted = permissionGranted)
            try {
                val response = NetworkClient.weatherApiService.getCurrentWeather(
                    latitude = DEFAULT_LAT,
                    longitude = DEFAULT_LON
                )
                val current = response.current
                val temp = current?.temperature2m ?: 28.0
                val humidity = current?.relativeHumidity2m ?: 82
                val windSpeed = current?.windSpeed10m ?: 3.0

                val weatherData = DashboardWeatherData(
                    locationName = locationName,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    isFallback = true
                )
                val advisories = advisoryService.getAdvisories(humidity, windSpeed)

                _uiState.value = DashboardUiState(
                    isLoading = false,
                    weather = weatherData,
                    advisories = advisories,
                    error = null,
                    locationPermissionGranted = permissionGranted
                )
            } catch (e: Exception) {
                val weatherData = DashboardWeatherData(
                    locationName = locationName,
                    temperature = 28.0,
                    humidity = 82,
                    windSpeed = 3.0,
                    isFallback = true
                )
                val advisories = advisoryService.getAdvisories(82, 3.0)
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    weather = weatherData,
                    advisories = advisories,
                    error = "Failed to fetch live weather (${e.localizedMessage}). Showing default values.",
                    locationPermissionGranted = permissionGranted
                )
            }
        }
    }

    private fun fetchWeatherForCoordinates(
        context: Context,
        lat: Double,
        lon: Double
    ) {
        viewModelScope.launch {
            try {
                val resolvedCity = getCityName(context, lat, lon)
                val locationName = if (resolvedCity.isBlank()) DEFAULT_LOCATION_NAME else resolvedCity

                val response = NetworkClient.weatherApiService.getCurrentWeather(
                    latitude = lat,
                    longitude = lon
                )

                val current = response.current
                val temp = current?.temperature2m ?: 28.0
                val humidity = current?.relativeHumidity2m ?: 82
                val windSpeed = current?.windSpeed10m ?: 3.0

                val weatherData = DashboardWeatherData(
                    locationName = locationName,
                    temperature = temp,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    isFallback = false
                )

                val advisories = advisoryService.getAdvisories(humidity, windSpeed)

                _uiState.value = DashboardUiState(
                    isLoading = false,
                    weather = weatherData,
                    advisories = advisories,
                    error = null,
                    locationPermissionGranted = true
                )
            } catch (e: Exception) {
                fetchFallbackData(context, DEFAULT_LOCATION_NAME)
            }
        }
    }

    private suspend fun getCityName(context: Context, lat: Double, lon: Double): String =
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine { continuation ->
                        try {
                            geocoder.getFromLocation(lat, lon, 1, object : Geocoder.GeocodeListener {
                                override fun onGeocode(addresses: MutableList<Address>) {
                                    val address = addresses.firstOrNull()
                                    val locality = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                                    val resultName = if (!locality.isNullOrBlank()) {
                                        val country = address?.countryName
                                        if (!country.isNullOrBlank()) "$locality, $country" else locality
                                    } else {
                                        DEFAULT_LOCATION_NAME
                                    }
                                    if (continuation.isActive) {
                                        continuation.resume(resultName)
                                    }
                                }

                                override fun onError(errorMessage: String?) {
                                    if (continuation.isActive) {
                                        continuation.resume(DEFAULT_LOCATION_NAME)
                                    }
                                }
                            })
                        } catch (e: Exception) {
                            if (continuation.isActive) {
                                continuation.resume(DEFAULT_LOCATION_NAME)
                            }
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lon, 1)
                    val address = addresses?.firstOrNull()
                    val locality = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                    if (!locality.isNullOrBlank()) {
                        val country = address?.countryName
                        if (!country.isNullOrBlank()) "$locality, $country" else locality
                    } else {
                        DEFAULT_LOCATION_NAME
                    }
                }
            } catch (e: Exception) {
                DEFAULT_LOCATION_NAME
            }
        }
}
