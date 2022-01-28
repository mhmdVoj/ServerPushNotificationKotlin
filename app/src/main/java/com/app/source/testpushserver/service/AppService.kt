package com.app.source.testpushserver.service

import com.app.source.testpushserver.model.ResponseModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AppService {

    @POST("/send-token")
    fun sendToken(@Body jsonObject: JsonObject) : Call<ResponseModel>
}