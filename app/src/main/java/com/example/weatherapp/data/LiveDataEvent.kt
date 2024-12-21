package com.example.weatherapp.data

data class LiveDataEvent<out T>(private val content: T) {

    private var hasBennHandled = false

    fun getContentIfNotHandled(): T? {
        return if(hasBennHandled) {
            null
        } else {
            hasBennHandled = true
            content
        }
    }
}