package com.cvirn.weathercvirn.model

data class DailyCityQuery(

    val lat: Double,
    val lon: Double,
    val appId: String,
    val exclude: String,
    val name: String
)
