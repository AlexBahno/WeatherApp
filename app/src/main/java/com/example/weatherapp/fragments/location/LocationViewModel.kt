package com.example.weatherapp.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.network.repository.WeatherDataRepository
import kotlinx.coroutines.launch

class LocationViewModel(private val weatherDataRepository: WeatherDataRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<SearchResultDataState>()
    val searchResult: LiveData<SearchResultDataState> get() = _searchResult

    fun searchLocation(query: String) {
        viewModelScope.launch {
            emitSearchResultUIState(isLoading = true)
            val searchResult = weatherDataRepository.searchLocation(query)
            if(searchResult.isNullOrEmpty()) {
                emitSearchResultUIState(error = R.string.search_error.toString())
            } else {
                emitSearchResultUIState(locations = searchResult)
            }
        }
    }

    private fun emitSearchResultUIState(
        isLoading: Boolean = false,
        locations: List<RemoteLocation>? = null,
        error: String? = null
    ) {
        val searchResultDataState = SearchResultDataState(isLoading, locations, error)
        _searchResult.value = searchResultDataState
    }

    data class SearchResultDataState(
        val isLoading: Boolean,
        val locations: List<RemoteLocation>?,
        val error: String?
    )
}