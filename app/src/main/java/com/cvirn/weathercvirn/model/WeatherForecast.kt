package com.cvirn.weathercvirn.model

data class WeatherForecast(
    val locationForecast: LocationForecast?,
    val isSuccess: Boolean
) {

    data class LocationForecast(

        val base: String?,
        val clouds: Clouds?,
        val cod: Int?,
        val coord: Coord?,
        val dt: Int?,
        val id: Int?,
        val main: Main?,
        val name: String?,
        val sys: Sys?,
        val timezone: Int?,
        val visibility: Int?,
        val weather: Weather?,
        val wind: Wind?
    ) {
        data class Clouds(
            val all: Int?
        )

        data class Coord(
            val lat: Double?,
            val lon: Double?
        )

        data class Main(
            val feelsLike: Double?,
            val humidity: Int?,
            val pressure: Int?,
            val temp: Double?,
            val tempMax: Double?,
            val tempMin: Double?
        )

        data class Sys(
            val country: String?,
            val message: Double?,
            val sunrise: Int?,
            val sunset: Int?,
            val type: Int?
        )

        data class Weather(
            val description: String?,
            val icon: String?,
            val main: String?
        )

        data class Wind(
            val deg: Int?,
            val speed: Double?
        )
    }
}
