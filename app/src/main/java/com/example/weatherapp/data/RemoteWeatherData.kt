package com.example.weatherapp.data

import com.example.weatherapp.fragments.home.HomeFragment
import com.google.gson.annotations.SerializedName

data class RemoteWeatherData(
    val current: CurrentWeatherRemote,
    val forecast: ForecastRemote
)

data class CurrentWeatherRemote(
    @SerializedName("temp_c") private val temperatureCelsius: Float,
    @SerializedName("temp_f") private val temperatureFahrenheit: Float,
    val condition: WeatherConditionRemote,
    @SerializedName("wind_kph") val wind: Float,
    val humidity: Int,
) {
    fun temperature(): Float {
        return if (HomeFragment.degreeType == DegreeType.CELSIUS) temperatureCelsius else temperatureFahrenheit
    }
}

data class ForecastRemote(
    @SerializedName("forecastday") val forecastDay: List<ForecastDayRemote>
)

data class ForecastDayRemote(
    val date: String,
    val day: DayRemote,
    val hour: List<ForecastHourRemote>
)

data class DayRemote(
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int
)

data class ForecastHourRemote(
    val time: String,
    @SerializedName("temp_c") private val temperatureCelsius: Float,
    @SerializedName("temp_f") private val temperatureFahrenheit: Float,
    @SerializedName("feelslike_c") private val feelsLikeTemperatureCelsius: Float,
    @SerializedName("feelslike_f") private val feelsLikeTemperatureFahrenheit: Float,
    val condition: WeatherConditionRemote,
) {
    fun temperature(): Float {
        return if (HomeFragment.degreeType == DegreeType.CELSIUS) temperatureCelsius else temperatureFahrenheit
    }

    fun feelsLikeTemperature(): Float {
        return if (HomeFragment.degreeType == DegreeType.CELSIUS) feelsLikeTemperatureCelsius else feelsLikeTemperatureFahrenheit
    }
}

data class WeatherConditionRemote(
    val icon: String
)