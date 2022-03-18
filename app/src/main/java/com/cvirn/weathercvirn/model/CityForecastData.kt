package com.cvirn.weathercvirn.model

data class CityForecastData(
    val cityForecast: CityForecast?,
    val isSuccess: Boolean
) {
    data class CityForecast(

        val daily: List<Daily>?,
        val name: String?,
        val timezone: String?,
        val timezoneOffset: Int?
    ) {
        data class Daily(

            val descriptionWeather: String?,
            val icon: String?,
            val id: Int?,
            val mainWeather: String?,

            val dayTemp: Double?,
            val eveTemp: Double?,
            val maxTemp: Double?,
            val minTemp: Double?,
            val mornTemp: Double?,
            val nightTemp: Double?,

            val clouds: Int?,
            val dewPoint: Double?,
            val dt: Int?,
            val humidity: Int?,

            val pop: Double?,
            val pressure: Double?,
            val rain: Double?,

            val windDeg: Int?,
            val windGust: Double?,
            val windSpeed: Double?
        )
    }
}
