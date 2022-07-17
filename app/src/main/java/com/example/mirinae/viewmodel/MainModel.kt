package com.example.mirinae.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.mirinae.module.RetrofitImpl
import com.example.mirinae.module.data.response.SaveRestaurantRes
import com.example.mirinae.module.data.response.getAllRestaurantRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainModel : ViewModel() {

    private val retrofit = RetrofitImpl.service
    private val requestCode = 2000

    fun preference(context:Context, activity:Activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                val dialog = AlertDialog.Builder(context)
                dialog.setMessage("앱을 이용하시려면 위치 권한을 허용 해 주세요.")
                dialog.setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
                })
                dialog.show()

            } else {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
            }
        }
    }

    fun saveRestaurant(title : String, content : String, latitude : Double, longitude : Double) {
        retrofit.saveRestaurant(title, content, latitude, longitude).enqueue(object : Callback<SaveRestaurantRes> {
            override fun onResponse(call: Call<SaveRestaurantRes>, response: Response<SaveRestaurantRes>) { Log.e("성공", "데이터 저장 성공")}
            override fun onFailure(call: Call<SaveRestaurantRes>, t: Throwable) { t.stackTrace }

        })
    }

    fun getAllMarkers(userId : String) {
        retrofit.getAllRestaurant(userId).enqueue(object : Callback<getAllRestaurantRes>{
            override fun onResponse(call: Call<getAllRestaurantRes>, response: Response<getAllRestaurantRes>) {
                val result = response.body()
                Log.e("data", result.toString())
            }

            override fun onFailure(call: Call<getAllRestaurantRes>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}