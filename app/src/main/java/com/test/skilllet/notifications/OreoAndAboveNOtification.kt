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
        pIntent: PendingIntent?,
        soundUri: Uri?,
        ref: String,
        subtext: String?
    ): Notification.Builder {
        return if (ref == "0") {
            Notification.Builder(applicationContext, ID)
                .setContentIntent(pIntent)
                .setContentTitle(title)
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(body)
                )
                .setSound(soundUri)
                .setSubText(subtext)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.logo)
        } else Notification.Builder(applicationContext, ID)
            .setContentIntent(pIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(soundUri)
            .setSubText(subtext)
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.logo)
    }

    companion object {
        private const val ID = "some_id"
        const val NAME = "FirebaseAPP"
    }

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}