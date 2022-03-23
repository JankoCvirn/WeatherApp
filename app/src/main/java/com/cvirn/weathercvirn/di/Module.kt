package com.cvirn.weathercvirn.di

import com.cvirn.weathercvirn.repository.DataStoreRepository
import com.cvirn.weathercvirn.repository.LocationRepository
import com.cvirn.weathercvirn.repository.NotificationRepository
import com.cvirn.weathercvirn.repository.WeatherRepository
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@JvmField
val appModule = module {

    single { WeatherRepository() }
    single { DataStoreRepository(get()) }
    single { LocationRepository(androidApplication().applicationContext) }
    single { NotificationRepository(androidApplication().applicationContext) }

    viewModel { MainViewModel(get(), get(), get()) }
}
