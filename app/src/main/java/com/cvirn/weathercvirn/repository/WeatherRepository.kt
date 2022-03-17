package com.cvirn.weathercvirn.repository

import com.cvirn.weathercvirn.client.APIClient
import com.cvirn.weathercvirn.model.CurrentLocation
import com.cvirn.weathercvirn.model.WeatherForecast
import com.cvirn.weathercvirn.response.WeatherResponse

class WeatherRepository {

    private val apiServices by lazy {
        APIClient.apiService()
    }

    suspend fun getWeatherForecast(currentLocation: CurrentLocation): WeatherForecast {
        val result = apiServices.requestWeatherForecast(
            lat = currentLocation.lat,
            lon = currentLocation.lon,
            appId = currentLocation.appId,
            units = currentLocation.units
        )

        return if (result.isSuccessful && result.body() != null) {
            mapWeatherResponse(result.body() as WeatherResponse)
        } else {
            WeatherForecast(
                locationForecast = null,
                isSuccess = false
            )
        }
    }
}
