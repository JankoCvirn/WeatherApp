package com.cvirn.weathercvirn.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cvirn.weathercvirn.model.WeatherForecast
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.isLocationProviderEnabled(): Boolean {
    val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

fun Context.isInternetAvailable(): Boolean {

    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.N)
fun Int.getDate(): String {
    val timestampString = this.toLong()
    return SimpleDateFormat("dd/MM/yyyy").format(Date(timestampString * 1000))
}

fun Int.isBadWeather(): Boolean {
    return this < 800
}

fun getTimestampId(): Long {
    return System.currentTimeMillis() / 1000
}

fun String.buildIconUrl(): String {
    return "https://openweathermap.org/img/wn/$this.png"
}

fun WeatherForecast.prepareCache(): String {
    return Gson().toJson(
        this
    )
}

fun String.unCacheWeatherForecast(): WeatherForecast {
    return Gson().fromJson(this, WeatherForecast::class.java)
}
