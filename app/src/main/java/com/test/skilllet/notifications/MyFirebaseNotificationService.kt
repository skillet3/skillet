package com.test.skilllet.notifications

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.test.skilllet.database.Repository
import com.test.skilllet.notifications.APIClient.apiService


class MyFirebaseNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      //      sendOAndAboveNOtification(remoteMessage)
        } else {
        //    sendNormalNOtification(remoteMessage)
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

//    private fun sendNormalNOtification(remoteMessage: RemoteMessage) {
//        val title = remoteMessage.data["title"]
//        val body = remoteMessage.data["message"]
//        var subText = ""
//        val ref = remoteMessage.data["intent"]
//        var intent: Intent? = null
//        if (AvailableServices.login) {
//            if (ref == "0") {
//                subText = "Congrats!"
//                val cusName = remoteMessage.data["cusName"]
//                val cusNo = remoteMessage.data["cusNo"]
//                intent = Intent(this, Orders::class.java)
//                intent.putExtra("phoneNumber", cusNo)
//                intent.putExtra("name", cusName)
//                intent.putExtra("tab", "1")
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            } else if (ref == "1") {
//                subText = "New Message!"
//                val cusName = remoteMessage.data["cusName"]
//                val cusNo = remoteMessage.data["cusNo"]
//                val proName = remoteMessage.data["proName"]
//                val proNo = remoteMessage.data["proNo"]
//                val orderNo = remoteMessage.data["orderNo"]
//                intent = Intent(this, Messaging::class.java)
//                intent.putExtra("cusNo", cusNo)
//                intent.putExtra("cusName", cusName)
//                intent.putExtra("proName", proName)
//                intent.putExtra("proNo", proNo)
//                intent.putExtra("orderNo", orderNo)
//                intent.putExtra("tab", "1")
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            }
//        } else {
//            intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        }
//        val pIntent = PendingIntent.getActivity(this, 92727, intent, PendingIntent.FLAG_ONE_SHOT)
//        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
//            .setSmallIcon(R.drawable.icon)
//            .setContentTitle(title)
//            .setContentIntent(pIntent)
//            .setSubText(subText)
//            .setAutoCancel(true)
//            .setPriority(Notification.PRIORITY_HIGH)
//            .setSound(soundUri)
//        if (ref == "0") {
//            builder.setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(body)
//            )
//        } else {
//            builder.setContentText(body)
//        }
//        val notificationManager =
//            getSystemService<Any>(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(92727, builder.build())
//    }

 //   @RequiresApi(api = Build.VERSION_CODES.O)
//    private fun sendOAndAboveNOtification(remoteMessage: RemoteMessage) {
//        val title = remoteMessage.data["title"]
//        val body = remoteMessage.data["message"]
//        var subText = ""
//        val ref = remoteMessage.data["intent"]
//        var intent: Intent? = null
//        if (AvailableServices.login) {
//            if (ref == "0") {
//                val cusName = remoteMessage.data["cusName"]
//                val cusNo = remoteMessage.data["cusNo"]
//                intent = Intent(this, Orders::class.java)
//                intent.putExtra("phoneNumber", cusNo)
//                intent.putExtra("name", cusName)
//                intent.putExtra("tab", "1")
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            } else if (ref == "1") {
//                val cusName = remoteMessage.data["cusName"]
//                val cusNo = remoteMessage.data["cusNo"]
//                val proName = remoteMessage.data["proName"]
//                val proNo = remoteMessage.data["proNo"]
//                val orderNo = remoteMessage.data["orderNo"]
//                intent = Intent(this, Messaging::class.java)
//                intent.putExtra("cusNo", cusNo)
//                intent.putExtra("cusName", cusName)
//                intent.putExtra("proName", proName)
//                intent.putExtra("proNo", proNo)
//                intent.putExtra("orderNo", orderNo)
//                intent.putExtra("tab", "1")
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            } else if (ref == "admin") {
//                val no = remoteMessage.data["no"]
//                val cmpID = remoteMessage.data["cmpID"]
//                intent = Intent(this, MessagingChat::class.java)
//                intent.putExtra("no", no)
//                subText = "New Complaint Message"
//                intent.putExtra("cmpID", cmpID)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            }
//        } else {
//            intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        }
//        if (ref == "0") {
//            subText = "Congrats!"
//        } else if (ref == "1") {
//            subText = "New Message!"
//        } else if (ref == "admin") {
//            subText = "New Complaint Message"
//        }
//        val pIntent = PendingIntent.getActivity(this, 92727, intent, PendingIntent.FLAG_ONE_SHOT)
//        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notification1 = OreoAndAboveNotification(this)
//        val builder: Notification.Builder = notification1.getNotifictions(
//            title, body, pIntent, soundUri,
//            ref!!, subText
//        )
//        notification1.manager!!.notify(92727, builder.build())
//    }


}