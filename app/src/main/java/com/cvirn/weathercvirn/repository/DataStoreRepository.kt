package com.cvirn.weathercvirn.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.cvirn.weathercvirn.model.LastLocation
import com.cvirn.weathercvirn.model.WeatherForecast
import com.cvirn.weathercvirn.utils.unCacheWeatherForecast
import com.cvirn.weathercvirn.viewmodel.ViewModelDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStoreRepository(val context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = DATA_STORE
    )

    fun setLocation(lat: Double, lon: Double) {
        CoroutineScope(ViewModelDispatcher.dispatcher).launch {
            dataStore.edit {
                it[LON] = lon
                it[LAT] = lat
            }
        }
    }

    fun setCachedForecast(forecast: String) {
        CoroutineScope(ViewModelDispatcher.dispatcher).launch {
            dataStore.edit {
                it[CACHED_FORECAST] = forecast
            }
        }
    }

    val getCachedForecast: Flow<WeatherForecast?>
        get() = dataStore.data.map {
            it[CACHED_FORECAST]?.unCacheWeatherForecast()
        }

    val getLastLocation: Flow<LastLocation>
        get() = dataStore.data.map {
            LastLocation(
                lon = it[LON] ?: 0.0,
                lat = it[LAT] ?: 0.0
            )
        }

    companion object {
        private const val DATA_STORE = "app_preferences"
        private val LAT = preferencesKey<Double>("lat")
        private val LON = preferencesKey<Double>("lon")
        private val CACHED_FORECAST = preferencesKey<String>("cached_forecast")
    }
}
