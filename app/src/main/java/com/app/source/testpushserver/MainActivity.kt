package com.app.source.testpushserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.source.testpushserver.model.ResponseModel
import com.app.source.testpushserver.service.AppService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.153.75:5001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(AppService::class.java)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val jsonObject = JsonObject()
            jsonObject.addProperty("uuid", 12)
            jsonObject.addProperty("token", token)
            jsonObject.addProperty("platform", "android")

            service.sendToken(jsonObject).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    Log.i("TAG", "onResponse: ${response.code()}")
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Log.i("TAG", "onResponse: $t")
                }

            })

            Log.d("TAG", token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })


        FirebaseMessaging.getInstance().subscribeToTopic("general")
            .addOnCompleteListener {
                var msg = "Subscribed Successfully"
                if (!it.isSuccessful) {
                    msg = "Subscription failed"
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }
}