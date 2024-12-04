package com.practice.android_engineer_intern_assignment

import LoginResponse
import Message
import MessageRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/login")
    fun login(@Body credentials: Map<String, String>): Call<LoginResponse>

    @GET("api/messages")
    fun getMessages(@Header("X-Branch-Auth-Token") token: String): Call<List<Message>>

    @POST("api/messages")
    fun sendMessage(
        @Header("X-Branch-Auth-Token") token: String,
        @Body message: MessageRequest
    ): Call<Message>
}
