package com.cvirn.weathercvirn.repository

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cvirn.weathercvirn.BuildConfig
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.utils.getTimestampId

class NotificationRepository(
    val context: Context
) {

    fun buildNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(context, BuildConfig.CHANNEL_ID)
            .setSmallIcon(R.drawable.icons8_weather_forecast_66)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(getTimestampId().toInt(), builder.build())
        }
    }
}
