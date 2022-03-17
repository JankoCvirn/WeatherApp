package com.cvirn.weathercvirn.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepository(
    private val context: Context
) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    fun lastLocationFlow(): Flow<Location> {
        return callbackFlow {
            val mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(UPDATES)
                .setInterval(INTERVAL)
                .setFastestInterval(fastestInterval)

            val mLocationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult.equals(null)) {
                        return
                    }
                    for (location in locationResult.locations) {
                        location?.let {
                            trySend(it).isSuccess
                        }
                    }
                }
            }
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.getMainLooper()
            )
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        trySend(it).isSuccess
                    }
                }
            awaitClose {}
        }
    }

    companion object {
        const val INTERVAL = 5_000L
        const val fastestInterval = 1_000L
        const val UPDATES = 1
    }
}
