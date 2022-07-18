package com.example.mirinae.module

import com.example.mirinae.module.data.request.SignInReq
import com.example.mirinae.module.data.request.SignUpReq
import com.example.mirinae.module.data.request.SaveRestaurantReq
import com.example.mirinae.module.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService
{
    @POST("/user/sign-in")
    fun login(@Header("Content-Type") json : String,
              @Body req : SignInReq) : Call<UserRes>

    @POST("/user/sign-up")
    fun signUp(@Header("Content-Type") json : String,
                @Body req : SignUpReq) : Call<UserRes>

    @POST("/restaurant/save/{userId}")
    fun saveRestaurant(@Header("Content-Type") json : String,
                       @Path("userId") userId: String,
                       @Body req : SaveRestaurantReq) : Call<SaveRestaurantRes>

    @GET("/restaurant/favorite/{userId}")
    fun getAllRestaurant(@Path("userId") userId : String) : Call<getAllRestaurantRes>

    @GET("/restaurant/search")
    fun searchRestaurant(@Query("title") title : String) : Call<getRestaurantRes>

    @DELETE("/restaurant/favorite/{title}")
    fun deleteRestaurant(@Path("title") title: String) : Call<DeleteRes>

    @DELETE("/user")
    fun withdrawal (@Path("userId") userId: String ) : Call<DeleteRes>

}