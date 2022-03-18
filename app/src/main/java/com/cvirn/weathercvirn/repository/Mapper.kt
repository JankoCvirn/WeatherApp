package com.cvirn.weathercvirn.repository

import com.cvirn.weathercvirn.model.CityForecastData
import com.cvirn.weathercvirn.model.WeatherForecast
import com.cvirn.weathercvirn.response.CityForecastResponse
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

fun mapCityForecastResponse(
    cityForecastResponse: CityForecastResponse,
    name: String
): CityForecastData {

    val dailyList = mapDailyList(cityForecastResponse.daily)

    return CityForecastData(
        isSuccess = true,
        cityForecast = CityForecastData.CityForecast(
            name = name,
            timezone = cityForecastResponse.timezone,
            timezoneOffset = cityForecastResponse.timezoneOffset,
            daily = dailyList
        )
    )
}

private fun mapDailyList(list: List<CityForecastResponse.Daily?>?): List<CityForecastData.CityForecast.Daily> {
    val daily = mutableListOf<CityForecastData.CityForecast.Daily>()
    list?.let { list1 ->
        list1.forEach {
            daily.add(
                CityForecastData.CityForecast.Daily(
                    descriptionWeather = it?.weather?.first()?.description,
                    clouds = it?.clouds,
                    mainWeather = it?.weather?.first()?.main,
                    icon = it?.weather?.first()?.icon,
                    maxTemp = it?.temp?.max,
                    minTemp = it?.temp?.min,
                    mornTemp = it?.temp?.morn,
                    dayTemp = it?.temp?.day,
                    eveTemp = it?.temp?.eve,
                    id = it?.weather?.first()?.id,
                    nightTemp = it?.temp?.night,
                    dewPoint = it?.dewPoint,
                    dt = it?.dt,
                    pressure = it?.pressure,
                    humidity = it?.humidity,
                    pop = it?.pop,
                    rain = it?.rain,
                    windDeg = it?.windDeg,
                    windGust = it?.windGust,
                    windSpeed = it?.windSpeed
                )
            )
        }
    }
    return daily.toList()
}
