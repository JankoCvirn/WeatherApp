package com.cvirn.weathercvirn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cvirn.weathercvirn.BuildConfig
import com.cvirn.weathercvirn.model.CurrentLocation
import com.cvirn.weathercvirn.model.Event
import com.cvirn.weathercvirn.model.WeatherForecast
import com.cvirn.weathercvirn.repository.LocationRepository
import com.cvirn.weathercvirn.repository.WeatherRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : BaseViewModel() {

    private val _forecastObservable = MutableLiveData<Event<WeatherForecast>>()
    val forecastObservable: LiveData<Event<WeatherForecast>> = _forecastObservable

    private val _progressObservable = MutableLiveData<Boolean>()
    val progressObservable: LiveData<Boolean> = _progressObservable

    private val _errorObservable = MutableLiveData<Boolean>()
    val errorObservable: LiveData<Boolean> = _errorObservable

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
                    _forecastObservable.value = Event(weatherForecast)
                } else {
                    _errorObservable.value = true
                }
            }
        }
    }

    fun getLastLocation(units: String) {
        scope.launch(handler) {
            locationRepository.lastLocationFlow().collect { location ->
                withContext(ViewModelDispatcher.uiDispatcher) {
                    queryCurrentLocationForecast(
                        CurrentLocation(
                            lat = location.latitude,
                            lon = location.longitude,
                            appId = BuildConfig.API_KEY,
                            units = units
                        )
                    )
                }
            }
        }
    }
}
