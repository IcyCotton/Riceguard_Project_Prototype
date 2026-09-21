package com.example.riceguard_project_prototype.data

import com.example.riceguard_project_prototype.data.model.WeatherResponse
import java.util.Locale

class AdvisoryService {
    fun getAdvisory(weather: WeatherResponse): List<String> {
        val advisories = mutableListOf<String>()
        
        // If humidity > 80%, suggest 'High Disease Risk' (especially for Rice Blast).
        if (weather.main.humidity > 80) {
            advisories.add("High Disease Risk (especially for Rice Blast) due to high humidity (${weather.main.humidity}%)")
        }
        
        // If wind speed > 20 km/h or raining, suggest 'Unfavorable Spraying Conditions'.
        // Note: wind speed in metric is meter/sec. 20 km/h ≈ 5.56 m/s
        val windSpeedKmh = weather.wind.speed * 3.6
        val isRaining = weather.weather.any { it.main.contains("Rain", ignoreCase = true) }
        
        if (windSpeedKmh > 20 || isRaining) {
            val reasons = mutableListOf<String>()
            if (windSpeedKmh > 20) reasons.add("high wind speed (${String.format(Locale.US, "%.1f", windSpeedKmh)} km/h)")
            if (isRaining) reasons.add("rain")
            
            advisories.add("Unfavorable Spraying Conditions due to ${reasons.joinToString(" and ")}.")
        }
        
        if (advisories.isEmpty()) {
            advisories.add("Conditions are favorable for crop maintenance.")
        }
        
        return advisories
    }
}
