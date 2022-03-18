package com.cvirn.weathercvirn.response

import com.google.gson.annotations.SerializedName

data class CityForecastResponse(
    @SerializedName("daily")
    val daily: List<Daily?>?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int?
) {
    data class Daily(
        @SerializedName("clouds")
        val clouds: Int?,
        @SerializedName("dew_point")
        val dewPoint: Double?,
        @SerializedName("dt")
        val dt: Int?,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike?,
        @SerializedName("humidity")
        val humidity: Int?,
        @SerializedName("moon_phase")
        val moonPhase: Double?,
        @SerializedName("moonrise")
        val moonrise: Int?,
        @SerializedName("moonset")
        val moonset: Int?,
        @SerializedName("pop")
        val pop: Int?,
        @SerializedName("pressure")
        val pressure: Int?,
        @SerializedName("rain")
        val rain: Double?,
        @SerializedName("sunrise")
        val sunrise: Int?,
        @SerializedName("sunset")
        val sunset: Int?,
        @SerializedName("temp")
        val temp: Temp?,
        @SerializedName("uvi")
        val uvi: Int?,
        @SerializedName("weather")
        val weather: List<Weather?>?,
        @SerializedName("wind_deg")
        val windDeg: Int?,
        @SerializedName("wind_gust")
        val windGust: Double?,
        @SerializedName("wind_speed")
        val windSpeed: Double?
    ) {
        data class FeelsLike(
            @SerializedName("day")
            val day: Double?,
            @SerializedName("eve")
            val eve: Double?,
            @SerializedName("morn")
            val morn: Double?,
            @SerializedName("night")
            val night: Double?
        )

        data class Temp(
            @SerializedName("day")
            val day: Double?,
            @SerializedName("eve")
            val eve: Double?,
            @SerializedName("max")
            val max: Double?,
            @SerializedName("min")
            val min: Double?,
            @SerializedName("morn")
            val morn: Double?,
            @SerializedName("night")
            val night: Double?
        )

        data class Weather(
            @SerializedName("description")
            val description: String?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("main")
            val main: String?
        )
    }
}