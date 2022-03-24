package com.cvirn.weathercvirn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.work.*
import com.cvirn.weathercvirn.BuildConfig
import com.cvirn.weathercvirn.model.*
import com.cvirn.weathercvirn.repository.DataStoreRepository
import com.cvirn.weathercvirn.repository.LocationRepository
import com.cvirn.weathercvirn.repository.WeatherRepository
import com.cvirn.weathercvirn.utils.prepareCache
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val dataStoreRepository: DataStoreRepository
) : BaseViewModel() {

    private val _forecastObservable = MutableLiveData<Event<WeatherForecast>>()
    val forecastObservable: LiveData<Event<WeatherForecast>> = _forecastObservable

    private val _cityWeatherObservable = MutableLiveData<Event<WeatherForecast>>()
    val cityWeatherObservable: LiveData<Event<WeatherForecast>> = _cityWeatherObservable

    private val _cityDailyWeatherObservable = MutableLiveData<CityForecastData>()
    val cityDailyWeatherObservable: LiveData<CityForecastData> = _cityDailyWeatherObservable

    private val _progressObservable = MutableLiveData<Boolean>()
    val progressObservable: LiveData<Boolean> = _progressObservable

    private val _errorObservable = MutableLiveData<Boolean>()
    val errorObservable: LiveData<Boolean> = _errorObservable

    private val _citySearchErrorObservable = MutableLiveData<Boolean>()
    val citySearchErrorObservable: LiveData<Boolean> = _citySearchErrorObservable

    val cachedForecast: LiveData<WeatherForecast?> =
        dataStoreRepository.getCachedForecast.asLiveData()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _errorObservable.postValue(true)
    }

    private fun queryCurrentLocationForecast(currentLocation: CurrentLocation) {
        _progressObservable.value = true
        scope.launch(handler) {
            val weatherForecast = weatherRepository.getWeatherForecast(currentLocation)
            withContext(ViewModelDispatcher.uiDispatcher) {
                _progressObservable.value = false
                if (weatherForecast.isSuccess) {
                    storeForecastInCache(weatherForecast)
                    _forecastObservable.value = Event(weatherForecast)
                } else {
                    _errorObservable.value = true
                }
            }
        }
    }

    private fun storeLocation(currentLocation: CurrentLocation) {
        dataStoreRepository.setLocation(
            lat = currentLocation.lat,
            lon = currentLocation.lon
        )
    }

    private fun storeForecastInCache(weatherForecast: WeatherForecast) {
        val cached = weatherForecast.prepareCache()
        dataStoreRepository.setCachedForecast(cached)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLastLocation(units: String) {
        scope.launch(handler) {
            locationRepository.lastLocationFlow().collect { location ->
                withContext(ViewModelDispatcher.uiDispatcher) {
                    val currentLocation = CurrentLocation(
                        lat = location.latitude,
                        lon = location.longitude,
                        appId = BuildConfig.API_KEY,
                        units = units
                    )
                    storeLocation(currentLocation)
                    queryCurrentLocationForecast(currentLocation)
                }
            }
        }
    }

    fun getCityWeather(cityQuery: CityQuery) {
        scope.launch(handler) {
            val weatherForecast = weatherRepository.getCityWeatherForecast(cityQuery)
            withContext(ViewModelDispatcher.uiDispatcher) {
                _progressObservable.value = false
                if (weatherForecast.isSuccess) {
                    _cityWeatherObservable.value = Event(weatherForecast)
                    getCityWeatherForecast(
                        DailyCityQuery(
                            lat = weatherForecast.locationForecast?.coord?.lat ?: 0.0,
                            lon = weatherForecast.locationForecast?.coord?.lon ?: 0.0,
                            appId = BuildConfig.API_KEY,
                            name = weatherForecast.locationForecast?.name ?: "",
                            exclude = "hourly,minutely,current"
                        )
                    )
                } else {
                    _citySearchErrorObservable.value = true
                }
            }
        }
    }

    private fun getCityWeatherForecast(dailyCityQuery: DailyCityQuery) {
        scope.launch(handler) {
            val cityDailyWeatherForecast =
                weatherRepository.getCityDailyWeatherForecast(dailyCityQuery)
            withContext(ViewModelDispatcher.uiDispatcher) {
                if (cityDailyWeatherForecast.isSuccess) {
                    _cityDailyWeatherObservable.value = cityDailyWeatherForecast
                } else {
                    _errorObservable.value = true
                }
            }
        }
    }
}
