package com.example.weatherapp.fragments.home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.data.CurrentLocation
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.Forecast
import com.example.weatherapp.data.LiveDataEvent
import com.example.weatherapp.data.WeatherDate
import com.example.weatherapp.network.repository.WeatherDataRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(private val weatherDataRepository: WeatherDataRepository) : ViewModel() {

    //region Current Location
    private val _currentLocation = MutableLiveData<LiveDataEvent<CurrentLocationDataState>>()
    val currentLocation: LiveData<LiveDataEvent<CurrentLocationDataState>> get() = _currentLocation

    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        viewModelScope.launch {
            emitCurrentLocationUIState(isLoading = true)
            weatherDataRepository.getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onSuccess = { currentLocation ->
                    updateAddressText(currentLocation, geocoder)
                },
                onFailure = {
                    emitCurrentLocationUIState(error = "Unable to fetch current location")
                }
            )
        }
    }

    private fun updateAddressText(currentLocation: CurrentLocation, geocoder: Geocoder) {
        viewModelScope.launch {
            runCatching {
                weatherDataRepository.updateAddressText(currentLocation, geocoder)
            }.onSuccess { location ->
                emitCurrentLocationUIState(currentLocation = location)
            }.onFailure {
                emitCurrentLocationUIState(
                    currentLocation = currentLocation.copy(
                        location = R.string.n_a.toString()
                    )
                )
            }
        }
    }

    private fun emitCurrentLocationUIState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        error: String? = null
    ) {
        val currentLocationDataState = CurrentLocationDataState(isLoading, currentLocation, error)
        _currentLocation.value = LiveDataEvent(currentLocationDataState)
    }

    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )
    //endregion

    //region Weather Data
    private val _weatherData = MutableLiveData<LiveDataEvent<WeatherDataState>>()
    val weatherData: LiveData<LiveDataEvent<WeatherDataState>> get() = _weatherData

    fun getWeatherData(latitude: Double, longitude: Double, date: Int) {
        viewModelScope.launch {
            emitWeatherDataUIState(isLoading = true)
            weatherDataRepository.getWeatherData(latitude, longitude)?.let { weatherData ->
                emitWeatherDataUIState(
                    currentWeather = CurrentWeather(
                        icon = weatherData.current.condition.icon,
                        temperature = weatherData.current.temperature(),
                        wind = weatherData.current.wind,
                        humidity = weatherData.current.humidity,
                        chanceOfRain = weatherData.forecast.forecastDay.first().day.chanceOfRain
                    ),
                    dates = WeatherDate(
                        weatherData.forecast.forecastDay.map {
                            getWeatherDate(it.date)
                        }
                    ),
                    forecast = weatherData.forecast.forecastDay[date].hour.map {
                        Forecast(
                            time = getForecastTime(it.time),
                            temperature = it.temperature(),
                            feelsLikeTemperature = it.feelsLikeTemperature(),
                            icon = it.condition.icon
                        )
                    }
                )
            } ?: emitWeatherDataUIState(error = "Unable to fetch weather data")
        }
    }

    private fun emitWeatherDataUIState(
        isLoading: Boolean = false,
        currentWeather: CurrentWeather? = null,
        dates: WeatherDate? = null,
        forecast: List<Forecast>? = null,
        error: String? = null
    ) {
        val weatherDataState = WeatherDataState(isLoading, currentWeather, dates, forecast, error)
        _weatherData.value = LiveDataEvent(weatherDataState)
    }

    data class WeatherDataState(
        val isLoading: Boolean,
        val currentWeather: CurrentWeather?,
        val dates: WeatherDate?,
        val forecast: List<Forecast>?,
        val error: String?
    )

    private fun getForecastTime(dateTime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = pattern.parse(dateTime) ?: return dateTime
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }

    private fun getWeatherDate(dateTime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = pattern.parse(dateTime) ?: return dateTime
        return SimpleDateFormat("MM.dd", Locale.getDefault()).format(date)
    }
    //endregion
}