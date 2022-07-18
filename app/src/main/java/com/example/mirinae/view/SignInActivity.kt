package com.example.mirinae.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.mirinae.R
import com.example.mirinae.module.ContentType
import com.example.mirinae.module.RetrofitImpl
import com.example.mirinae.module.User
import com.example.mirinae.module.data.request.SignInReq
import com.example.mirinae.module.data.response.UserRes
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private val retrofit = RetrofitImpl.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val loginBtn = findViewById<View>(R.id.sign_in_login)
        val goSignUp = findViewById<TextView>(R.id.go_sign_up)

        loginBtn.setOnClickListener{
            val id = findViewById<TextView>(R.id.sign_in_id).text.toString()
            val pw = findViewById<TextView>(R.id.sign_in_pw).text.toString()

            val params = SignInReq(id,pw)

            retrofit.login(ContentType.CONTENT_TYPE , params).enqueue(object  : retrofit2.Callback<UserRes>{
                override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                    if(response.isSuccessful) {
                        Log.e("login", response.body()!!.userData.name)
                        User.userId = response.body()!!.userData.userId
                        User.name = response.body()!!.userData.name
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignInActivity, "아이디 또는 비밀번호가 다릅니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserRes>, t: Throwable) { Log.e("이유", t.message.toString()) }
            })
        }

        goSignUp.setOnClickListener{ startActivity(Intent(this@SignInActivity, SignUpActivity::class.java)) }
    }
}
