package com.example.weatherapp.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.data.CurrentLocation
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.Forecast
import com.example.weatherapp.data.WeatherData
import com.example.weatherapp.data.WeatherDate
import com.example.weatherapp.databinding.ItemContainerChooseDateBinding
import com.example.weatherapp.databinding.ItemContainerCurrentLocationBinding
import com.example.weatherapp.databinding.ItemContainerCurrentWeatherBinding
import com.example.weatherapp.databinding.ItemContainerForecastBinding

class WeatherDataAdapter(
    private val onLocationClicked: () -> Unit,
    private val onSettingsClicked: () -> Unit,
    private val onDateClicked: (index: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val INDEX_CURRENT_LOCATION = 0
        const val INDEX_CURRENT_WEATHER = 1
        const val INDEX_DATES = 2
        const val INDEX_FORECAST = 3
    }

    private val weatherData = mutableListOf<WeatherData>()

    fun setCurrentLocation(currentLocation: CurrentLocation) {
        if(weatherData.isEmpty()) {
            weatherData.add(INDEX_CURRENT_LOCATION, currentLocation)
            notifyItemInserted(INDEX_CURRENT_LOCATION)
        } else {
            weatherData[INDEX_CURRENT_LOCATION] = currentLocation
            notifyItemChanged(INDEX_CURRENT_LOCATION)
        }
    }

    fun setCurrentWeather(currentWeather: CurrentWeather) {
        if (weatherData.getOrNull(INDEX_CURRENT_WEATHER) != null) {
            weatherData[INDEX_CURRENT_WEATHER] = currentWeather
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        } else {
            weatherData.add(INDEX_CURRENT_WEATHER, currentWeather)
            notifyItemInserted(INDEX_CURRENT_WEATHER)
        }
    }

    fun setWeatherDates(dates: WeatherDate) {
        if (weatherData.getOrNull(INDEX_DATES) != null) {
            weatherData[INDEX_DATES] = dates
            notifyItemChanged(INDEX_DATES)
        } else {
            weatherData.add(INDEX_DATES, dates)
            notifyItemInserted(INDEX_DATES)
        }
    }

    fun setForecastData(forecast: List<Forecast>) {
        weatherData.removeAll { it is Forecast }
        notifyItemRangeRemoved(INDEX_FORECAST, weatherData.size)
        weatherData.addAll(INDEX_FORECAST, forecast)
        notifyItemRangeChanged(INDEX_FORECAST, weatherData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemContainerCurrentLocationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            INDEX_FORECAST -> ForecastViewHolder(
                ItemContainerForecastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            INDEX_DATES -> WeatherDateViewHolder(
                ItemContainerChooseDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> CurrentWeatherViewHolder(
                ItemContainerCurrentWeatherBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CurrentLocationViewHolder -> holder.bind(weatherData[position] as CurrentLocation)
            is CurrentWeatherViewHolder -> holder.bind(weatherData[position] as CurrentWeather)
            is ForecastViewHolder -> holder.bind(weatherData[position] as Forecast)
            is WeatherDateViewHolder -> holder.bind(weatherData[position] as WeatherDate)
        }
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(weatherData[position]) {
            is CurrentLocation -> INDEX_CURRENT_LOCATION
            is CurrentWeather -> INDEX_CURRENT_WEATHER
            is Forecast -> INDEX_FORECAST
            is WeatherDate -> INDEX_DATES
        }
    }

    inner class CurrentLocationViewHolder(
        private val binding: ItemContainerCurrentLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentLocation: CurrentLocation) {
            with(binding) {
                textCurrentDate.text = currentLocation.date
                textCurrentLocation.text = currentLocation.getLocation(binding.root.context)
                settings.setOnClickListener { onSettingsClicked() }
                textCurrentLocation.setOnClickListener { onLocationClicked() }
            }
        }
    }

    inner class CurrentWeatherViewHolder(
        private val binding: ItemContainerCurrentWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentWeather: CurrentWeather) {
            with(binding) {
                imageIcon.load("https:${currentWeather.icon}") { crossfade(true) }
                textTemperature.text = String.format(HomeFragment.degreeType.getSymbol(), currentWeather.temperature)
                textWind.text = String.format("%s ${binding.root.context.resources.getString(R.string.km_h)}", currentWeather.wind)
                textHumidity.text = String.format("%s%%", currentWeather.humidity)
                textChanceOfRain.text = String.format("%s%%", currentWeather.chanceOfRain)
            }
        }
    }

    inner class ForecastViewHolder(
        private val binding: ItemContainerForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: Forecast) {
            with(binding) {
                textTime.text = forecast.time
                textTemperature.text = String.format(HomeFragment.degreeType.getSymbol(), forecast.temperature)
                textFeelsLikeTemperature.text = String.format(HomeFragment.degreeType.getSymbol(), forecast.feelsLikeTemperature)
                imageIcon.load("https:${forecast.icon}") { crossfade(true) }
            }
        }
    }

    inner class WeatherDateViewHolder(
        private val binding: ItemContainerChooseDateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dates: WeatherDate) {
            with(binding) {
                firstDate.text = dates.dates[0]
                firstDate.setOnClickListener { onDateClicked(0) }

                secondDate.text = dates.dates[1]
                secondDate.setOnClickListener { onDateClicked(1) }

                thirdDate.text = dates.dates[2]
                thirdDate.setOnClickListener { onDateClicked(2) }

                fourthDate.text = dates.dates[3]
                fourthDate.setOnClickListener { onDateClicked(3) }

                fifthDate.text = dates.dates[4]
                fifthDate.setOnClickListener { onDateClicked(4) }
            }
        }
    }
}