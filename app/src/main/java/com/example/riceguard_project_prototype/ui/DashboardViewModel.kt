package com.example.riceguard_project_prototype.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riceguard_project_prototype.data.AdvisoryService
import com.example.riceguard_project_prototype.data.model.WeatherResponse
import com.example.riceguard_project_prototype.data.model.Main
import com.example.riceguard_project_prototype.data.model.Wind
import com.example.riceguard_project_prototype.data.model.Weather
import com.example.riceguard_project_prototype.data.network.NetworkClient
import com.example.riceguard_project_prototype.data.network.Secrets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class DashboardUiState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val advisories: List<String> = emptyList(),
    val error: String? = null
)

class DashboardViewModel(
    private val advisoryService: AdvisoryService = AdvisoryService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = NetworkClient.weatherApiService.getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    apiKey = Secrets.OPEN_WEATHER_API_KEY
                )
                val advisories = advisoryService.getAdvisory(response)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weather = response,
                    advisories = advisories
                )
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    val fallbackWeather = WeatherResponse(
                        main = Main(temp = 28.0, humidity = 82),
                        wind = Wind(speed = 3.0),
                        weather = listOf(Weather(main = "Clouds", description = "Partly Sunny", icon = "03d")),
                        name = "Mock Location"
                    )
                    val advisories = advisoryService.getAdvisory(fallbackWeather)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weather = fallbackWeather,
                        advisories = advisories,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to fetch weather: ${e.message}"
                    )
                }
            }
        }
    }
}
