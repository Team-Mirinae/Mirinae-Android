package com.example.mirinae.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mirinae.module.RetrofitImpl
import com.example.mirinae.module.User
import com.example.mirinae.module.data.RestaurantData
import com.example.mirinae.module.data.response.DeleteRes
import com.example.mirinae.module.data.response.SaveRestaurantRes
import com.example.mirinae.module.data.response.getAllRestaurantRes
import com.example.mirinae.module.data.response.getRestaurantRes
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainModel : ViewModel() {

    private val retrofit = RetrofitImpl.service
    private val requestCode = 2000
    private val username = User.name
    private val userId = User.userId

    var job : Job? = null

    val markers = MutableLiveData<List<RestaurantData>>()

    fun refresh() {
        getAllMarkers(username!!)
    }

    fun preference(context:Context, activity:Activity) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                val dialog = AlertDialog.Builder(context)
                dialog.setMessage("앱을 이용하시려면 위치 권한을 허용 해 주세요.")
                dialog.setPositiveButton("확인") { _, _ ->
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
                }
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

    private fun getAllMarkers(userId : String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            retrofit.getAllRestaurant(userId).enqueue(object : Callback<getAllRestaurantRes>{
                override fun onResponse(call: Call<getAllRestaurantRes>, response: Response<getAllRestaurantRes>) {
                    markers.value = response.body()!!.list
                }
                override fun onFailure(call: Call<getAllRestaurantRes>, t: Throwable) { t.stackTrace }
            })
        }
    }

    suspend fun getMarker(title : String) {
        val result =
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                retrofit.searchRestaurant(title).enqueue(object : Callback<getRestaurantRes> {
                    override fun onResponse(call: Call<getRestaurantRes>, response: Response<getRestaurantRes>
                    ) {
                        response.body()!!.restaurantData
                    }

                    override fun onFailure(call: Call<getRestaurantRes>, t: Throwable) {
                        t.stackTrace
                    }
                })
            }
    }


    fun deleteMarker(title : String) {
        retrofit.deleteRestaurant(title).enqueue(object : Callback<DeleteRes>{
            override fun onResponse(call: Call<DeleteRes>, response: Response<DeleteRes>) {
                Log.e("삭제 성공", response.body()!!.msg)
            }
            override fun onFailure(call: Call<DeleteRes>, t: Throwable) { t.stackTrace }
        })
    }

}