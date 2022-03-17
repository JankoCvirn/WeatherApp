package com.cvirn.weathercvirn.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION
)

fun Context.allPermissionsGranted(): Boolean {
    return REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.requestPermissions(requestCode: Int) {
    ActivityCompat.requestPermissions(
        this, REQUIRED_PERMISSIONS,
        requestCode
    )
}
