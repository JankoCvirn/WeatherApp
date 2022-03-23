package com.cvirn.weathercvirn.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cvirn.weathercvirn.repository.WeatherRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ForecastWorker(
    context: Context,
    userParameters: WorkerParameters
) : CoroutineWorker(context,userParameters), KoinComponent {

    private val weatherRepository: WeatherRepository by inject()
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}