package com.example.riceguard_project_prototype.data

import java.util.Locale

class AdvisoryService {
    fun getAdvisories(humidity: Int, windSpeedMs: Double): List<String> {
        val advisories = mutableListOf<String>()

        // Disease Risk based on relative humidity (> 80%)
        if (humidity > 80) {
            advisories.add("⚠️ High Disease Risk Warning: Elevated relative humidity ($humidity%) creates ideal conditions for Fungal/Bacterial Blast threat.")
        } else {
            advisories.add("✅ Standard Monitoring: Relative humidity is $humidity%. Fungal/Bacterial Blast threat level is low.")
        }

        // Spraying conditions based on wind speed
        val windSpeedKmh = windSpeedMs * 3.6
        if (windSpeedKmh > 20) {
            advisories.add("⚠️ Unfavorable Spraying Conditions: Wind speed is high (${String.format(Locale.US, "%.1f", windSpeedMs)} m/s). Avoid chemical application to prevent drift.")
        } else {
            advisories.add("💡 Favorable Spraying Window: Wind speeds (${String.format(Locale.US, "%.1f", windSpeedMs)} m/s) are optimal for crop treatment.")
        }

        return advisories
    }
}
