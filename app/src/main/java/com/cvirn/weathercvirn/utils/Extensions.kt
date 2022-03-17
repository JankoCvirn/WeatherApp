package com.cvirn.weathercvirn.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast

import com.cvirn.weathercvirn.R

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

