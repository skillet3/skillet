package com.test.skilllet.notifications

import com.google.gson.JsonObject
import com.test.skilllet.BuildConfig
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface APIService {
    @Headers("Authorization: key=" + BuildConfig.FCM_SERVER_KEY, "Content-Type: application/json")
    @POST("fcm/send")
    fun sendNotification(@Body payload: JsonObject?): Call<JsonObject?>?
}