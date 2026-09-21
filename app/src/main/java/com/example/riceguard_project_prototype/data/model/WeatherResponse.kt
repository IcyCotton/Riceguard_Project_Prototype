package com.example.riceguard_project_prototype.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "main") val main: Main,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp") val temp: Double,
    @Json(name = "humidity") val humidity: Int
)

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed") val speed: Double
)

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)
