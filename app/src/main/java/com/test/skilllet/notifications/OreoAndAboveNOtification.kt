package com.test.skilllet.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.test.skilllet.R


class OreoAndAboveNotification(base: Context?) :
    ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel() {
        val notificationChannel = NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager!!.createNotificationChannel(notificationChannel)
    }

    val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    override fun getApplicationInfo(): ApplicationInfo {
        return super.getApplicationInfo()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getNotifictions(
        title: String?,
        body: String?,
        soundUri: Uri?
    ): Notification.Builder {
         return   Notification.Builder(applicationContext, ID)
                .setContentTitle(title)
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(body)
                )
             .setContentText(body)
             .setSubText(body)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.logo)

    }

    companion object {
        private const val ID = "notification_channel"
        const val NAME = "FirebaseAPP"
    }

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}