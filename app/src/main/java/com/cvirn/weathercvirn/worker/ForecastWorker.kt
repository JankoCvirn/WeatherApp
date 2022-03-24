package com.cvirn.weathercvirn.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cvirn.weathercvirn.BuildConfig
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.model.CurrentLocation
import com.cvirn.weathercvirn.repository.DataStoreRepository
import com.cvirn.weathercvirn.repository.NotificationRepository
import com.cvirn.weathercvirn.repository.WeatherRepository
import com.cvirn.weathercvirn.utils.isBadWeather
import com.cvirn.weathercvirn.utils.prepareCache
import com.cvirn.weathercvirn.viewmodel.ViewModelDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ForecastWorker(
    private val context: Context,
    userParameters: WorkerParameters
) : CoroutineWorker(context, userParameters), KoinComponent {

    private val weatherRepository: WeatherRepository by inject()
    private val dataStoreRepository: DataStoreRepository by inject()
    private val notificationRepository: NotificationRepository by inject()
    override suspend fun doWork(): Result {
        CoroutineScope(ViewModelDispatcher.dispatcher).launch {
            val lastLocation = dataStoreRepository.getLastLocation.first()

            val currentLocation = CurrentLocation(
                lat = lastLocation.lat,
                lon = lastLocation.lon,
                appId = BuildConfig.API_KEY,
                units = "metric"
            )
            val weatherForecast = weatherRepository.getWeatherForecast(currentLocation)
            if (weatherForecast.isSuccess) {
                dataStoreRepository.setCachedForecast(weatherForecast.prepareCache())
                if (weatherForecast.locationForecast?.weather?.id?.isBadWeather() == false) {
                    notificationRepository.buildNotification(
                        context.resources.getString(R.string.danger),
                        context.resources.getString(
                            R.string.current_location_weather_placeholder,
                            weatherForecast.locationForecast.weather.main,
                            weatherForecast.locationForecast.weather.description
                        )
                    )
                }
            }
        }
        return Result.success()
    }
}
