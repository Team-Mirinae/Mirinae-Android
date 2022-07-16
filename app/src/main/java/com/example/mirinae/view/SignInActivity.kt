package com.example.mirinae.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mirinae.R
import com.example.mirinae.module.RetrofitImpl
import com.example.mirinae.module.RetrofitService
import com.example.mirinae.module.data.LoginData
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private val retrofit: RetrofitService = RetrofitImpl.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        retrofit.login("testId","test1234").enqueue(object  : retrofit2.Callback<LoginData>{
            override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                Log.e("데이터", "id = ${response.body()!!.id}, pw = ${response.body()!!.pw}")
            }

            override fun onFailure(call: Call<LoginData>, t: Throwable) {
                t.stackTrace
            }

        })
    }
}