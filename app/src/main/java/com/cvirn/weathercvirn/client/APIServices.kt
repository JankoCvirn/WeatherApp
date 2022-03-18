package com.cvirn.weathercvirn.client

import com.cvirn.weathercvirn.response.CityForecastResponse
import com.cvirn.weathercvirn.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIServices {

    @Headers("Accept: application/json")
    @GET("/data/2.5/weather")
    suspend fun requestWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

    @Headers("Accept: application/json")
    @GET("/data/2.5/weather")
    suspend fun requestCityWeatherForecast(
        @Query("q") cityName: String,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

    //https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,minutely,current&appid=befc2ae04c280e5d3b798259c1003c76
    @Headers("Accept: application/json")
    @GET("/data/2.5/onecall")
    suspend fun requestCityDailyWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("exclude") exclude: String,
    ): Response<CityForecastResponse>
}
