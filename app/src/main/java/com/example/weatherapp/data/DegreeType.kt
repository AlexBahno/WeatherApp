package com.example.weatherapp.data

enum class DegreeType {
    CELSIUS,
    FAHRENHEIT;

    fun getSymbol(): String {
        return when(this) {
            DegreeType.CELSIUS -> "%s\u00B0C"
            DegreeType.FAHRENHEIT -> "%s\u00B0F"
        }
    }
}