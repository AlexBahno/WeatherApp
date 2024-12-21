package com.example.weatherapp.data

import android.content.Context
import android.content.res.Resources
import android.health.connect.datatypes.units.Temperature
import androidx.annotation.StringRes
import com.example.weatherapp.R
import com.example.weatherapp.utils.AppConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class WeatherData

data class CurrentLocation(
    val date: String = getCurrentDate(),
    private val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) : WeatherData() {
    fun getLocation(context: Context): String {
        return if (location == null || location.trim().isEmpty()) {
            context.getString(R.string.choose_your_location)
        } else {
            location
        }
    }
}

data class Forecast(
    val time: String,
    val temperature: Float,
    val feelsLikeTemperature: Float,
    val icon: String
) : WeatherData()

data class WeatherDate(
    val dates: List<String>
) : WeatherData()

data class CurrentWeather(
    val icon: String,
    val temperature: Float,
    val wind: Float,
    val humidity: Int,
    val chanceOfRain: Int
) : WeatherData()

private fun getCurrentDate() : String {
    val currentDate = Date()
    val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    return formatter.format(currentDate)
}