package com.example.mirinae.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mirinae.R
import com.example.mirinae.module.RetrofitImpl
import com.example.mirinae.module.User
import com.example.mirinae.module.data.response.UserRes
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private val retrofit = RetrofitImpl.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        retrofit.login("testId","test1234").enqueue(object  : retrofit2.Callback<UserRes>{
            override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                Log.e("데이터", "id = ${response.body()!!.userData.userId}, pw = ${response.body()!!.userData.pw}, name = ${response.body()!!.userData.name}")
                User.userId = response.body()!!.userData.userId
                User.name = response.body()!!.userData.name

                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            }

            override fun onFailure(call: Call<UserRes>, t: Throwable) {
                t.stackTrace
            }

        })
    }
}