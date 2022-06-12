package com.test.skilllet.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.notifications.APIClient.apiService


class MyFirebaseNotificationService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           sendOAndAboveNOtification(remoteMessage)
        } else {
            sendNormalNotification(remoteMessage)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val databaseReference = FirebaseDatabase.getInstance().reference
        Repository.loggedInUser?.token=token
        Repository.loggedInUser?.accType?.let { accType ->
            Repository.loggedInUser?.key?.let { key ->
                databaseReference.child(accType).child(key)
                    .setValue(Repository.loggedInUser)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendNormalNotification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["msg"]

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentText(body)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(92727, builder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun sendOAndAboveNOtification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["msg"]
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification1 = OreoAndAboveNotification(this)
        val builder: Notification.Builder = notification1.getNotifictions(
            title, body,  soundUri
        )
        notification1.manager!!.notify(92727, builder.build())
    }


}