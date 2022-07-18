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
import com.example.mirinae.module.data.request.SignUpReq
import com.example.mirinae.module.data.response.UserRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private val retrofit = RetrofitImpl.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val nextBtn = findViewById<View>(R.id.next)

        nextBtn.setOnClickListener {
            val id = findViewById<TextView>(R.id.sign_up_id).text.toString()
            val pw = findViewById<TextView>(R.id.sign_up_pw).text.toString()
            val name = findViewById<TextView>(R.id.sign_up_name).text.toString()

            val params = SignUpReq(id, pw,name)

            retrofit.signUp(ContentType.CONTENT_TYPE, params).enqueue(object : Callback<UserRes> {
                override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                    if (response.isSuccessful) {
                        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                        finish()
                    } else Toast.makeText(this@SignUpActivity,"회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<UserRes>, t: Throwable) { Log.e("실패", t.message.toString()) }

            })
        }

    }
}