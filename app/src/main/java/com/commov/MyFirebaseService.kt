package com.commov

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseService : FirebaseMessagingService() {

    private val CHANNEL_ID: String = "420"

    override fun onMessageReceived(p0: RemoteMessage) {
        when(p0!!.from) {
            "/topics/issue" -> {
                var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_add_black_24dp)
                    .setContentTitle(p0.notification!!.title)
                    .setContentText(p0.notification!!.body)
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(p0.notification!!.body))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(Random(p0.sentTime).nextInt(), builder.build())
                }


                println("From: " + p0!!.from)
                println("Notification Message Body: " + p0.notification!!.body!!)

            }
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println(p0)
    }
}