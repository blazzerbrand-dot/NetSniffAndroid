package com.example.netsniffandroid.core.notification
//imports for notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import  com.example.netsniffandroid.R
import com.example.netsniffandroid.core.utils.NotificationConstants

// creating an object for notifications
object NotificationHelper {
    //creating notification Channel

    fun createNotificationChannel(
        context: Context
     ) {
        //notification channels are required starting from android 8.0 (API 26)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //configure the notification channel with low importance to prevent intrusive sounds
            val channel =
                NotificationChannel(
                    NotificationConstants.CHANNEL_ID,
                    NotificationConstants.CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
                )
             //retrieve the system's notificationManager service instance

            val manager =
                context.getSystemService(
                    NotificationManager::class.java
                )

            //register the newly configured channel with android operating system
            manager.createNotificationChannel((channel))
        }
    }

    /**
     * construct and configures the persistent background notification  for the VPN service
     *
     */
    fun buildVpnNotification(
        context:Context

    ): Notification{
        //Initialize the notification builder using the designated channel id
        return NotificationCompat.Builder(
            context,
            NotificationConstants.CHANNEL_ID
        )

        //set the primary headline text fot the notification
            .setContentTitle("NETSNIFF ACTIVE")
        //SET THE secondary descriptive subtext
            .setContentText(
                "MOnitoring device network traffic"
            )
        //define the status bar icon
            .setSmallIcon(R.mipmap.ic_launcher)

        //make the notification persistent so the user cannot swipe it away
            .setOngoing(true)
            .build() //finalize  and compile the complete Notification object



    }
}
