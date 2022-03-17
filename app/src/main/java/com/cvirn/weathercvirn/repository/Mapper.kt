package com.cvirn.weathercvirn.repository

import com.cvirn.weathercvirn.model.WeatherForecast
import com.cvirn.weathercvirn.response.WeatherResponse

fun mapWeatherResponse(weatherResponse: WeatherResponse): WeatherForecast {

    return WeatherForecast(
        isSuccess = true,
        locationForecast = WeatherForecast.LocationForecast(
            name = weatherResponse.name,
            base = weatherResponse.base,
            clouds = WeatherForecast.LocationForecast.Clouds(
                all = weatherResponse.clouds?.all
            ),
            cod = weatherResponse.cod,
            dt = weatherResponse.dt,
            id = weatherResponse.id,
            timezone = weatherResponse.timezone,
            visibility = weatherResponse.visibility,
            wind = WeatherForecast.LocationForecast.Wind(
                speed = weatherResponse.wind?.speed,
                deg = weatherResponse.wind?.deg
            ),
            main = WeatherForecast.LocationForecast.Main(
                feelsLike = weatherResponse.main?.feelsLike,
                humidity = weatherResponse.main?.humidity,
                pressure = weatherResponse.main?.pressure,
                temp = weatherResponse.main?.temp,
                tempMax = weatherResponse.main?.tempMax,
                tempMin = weatherResponse.main?.tempMin
            ),
            weather = WeatherForecast.LocationForecast.Weather(
                description = weatherResponse.weather?.first()?.description,
                icon = weatherResponse.weather?.first()?.icon,
                main = weatherResponse.weather?.first()?.main
            ),
            sys = WeatherForecast.LocationForecast.Sys(
                sunrise = weatherResponse.sys?.sunrise,
                sunset = weatherResponse.sys?.sunset,
                country = weatherResponse.sys?.country,
                message = weatherResponse.sys?.message,
                type = weatherResponse.sys?.type
            ),
            coord = WeatherForecast.LocationForecast.Coord(
                lat = weatherResponse.coord?.lat,
                lon = weatherResponse.coord?.lon
            )
        )
    )
}
