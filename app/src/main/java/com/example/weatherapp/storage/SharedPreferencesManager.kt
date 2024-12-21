package com.example.weatherapp.storage

import android.content.Context
import androidx.core.content.edit
import com.example.weatherapp.data.CurrentLocation
import com.example.weatherapp.data.DegreeType
import com.google.gson.Gson

class SharedPreferencesManager(context: Context, private val gson: Gson) {

    private companion object {
        const val PREF_NAME = "WeatherAppPref"
        const val KEY_CURRENT_LOCATION = "currentLocation"
        const val KEY_DEGREES_TYPE = "degreeType"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveCurrentLocation(currentLocation: CurrentLocation) {
        val currentLocationJson = gson.toJson(currentLocation)
        sharedPreferences.edit {
            putString(KEY_CURRENT_LOCATION, currentLocationJson)
        }
    }

    fun getCurrentLocation(): CurrentLocation? {
        return sharedPreferences.getString(
            KEY_CURRENT_LOCATION,
            null
        )?.let { currentLocationJson ->
            gson.fromJson(currentLocationJson, CurrentLocation::class.java)
        }
    }

    fun saveDegreeType(degree: DegreeType) {
        val currentDegreeType = gson.toJson(degree)
        sharedPreferences.edit {
            putString(KEY_DEGREES_TYPE, currentDegreeType)
        }
    }

    fun getDegreeType(): DegreeType? {
        return sharedPreferences.getString(
            KEY_DEGREES_TYPE,
            null
        )?.let { currentDegreeType ->
            gson.fromJson(currentDegreeType, DegreeType::class.java)
        }
    }
}