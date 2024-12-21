package com.example.weatherapp.network.api

import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.data.RemoteWeatherData
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "da1dae1e49174c349aa170725241712"
    }

    @GET("search.json")
    suspend fun searchLocation(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<List<RemoteLocation>>

    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String,
        @Query("days") days: Int = 5,
        @Query("lang") lang: String = "en"
    ) : Response<RemoteWeatherData>
}