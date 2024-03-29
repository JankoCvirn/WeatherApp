package com.cvirn.weathercvirn.repository

import com.cvirn.weathercvirn.client.APIClient
import com.cvirn.weathercvirn.model.*
import com.cvirn.weathercvirn.response.CityForecastResponse
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

    suspend fun getCityWeatherForecast(cityQuery: CityQuery): WeatherForecast {
        val result = apiServices.requestCityWeatherForecast(
            cityName = cityQuery.city,
            appId = cityQuery.appId,
            units = cityQuery.units
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

    suspend fun getCityDailyWeatherForecast(dailyCityForecast: DailyCityQuery): CityForecastData {
        val result = apiServices.requestCityDailyWeatherForecast(
            lat = dailyCityForecast.lat,
            lon = dailyCityForecast.lon,
            exclude = dailyCityForecast.exclude,
            appId = dailyCityForecast.appId
        )

        return if (result.isSuccessful && result.body() != null) {
            mapCityForecastResponse(result.body() as CityForecastResponse, dailyCityForecast.name)
        } else {
            CityForecastData(
                cityForecast = null,
                isSuccess = false
            )
        }
    }
}
