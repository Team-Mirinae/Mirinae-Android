package com.example.mirinae.module

import com.example.mirinae.module.data.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService
{
    @POST("/user/sign-in")
    fun login(@Field("id") id : String,
              @Field("pw") pw : String) : Call<LoginData>

    @POST("/user/sign-up")
    fun signUp(@Field("id") id : String,
               @Field("pw") pw : String,
               @Field("name") name : String
               ) : Call<SignUpData>

    @POST("/restaurant/save")
    fun saveRestaurant(@Field("title") title : String,
                      @Field("content") content : String,
                      @Field("latitude") latitude : Double,
                      @Field("longitude") longitude : Double) : Call<SaveRestaurantData>

    @GET("/restaurant/favorite")
    fun getAllRestaurant(@Path("userId") userId : String) : Call<UserData>

    @GET("/restaurant/search")
    fun searchRestaurant(@Query("title") title : String) : Call<RestaurantData>

    @DELETE("/restaurant/favorite")
    fun deleteRestaurant(@Path("title") title : String) : Call<RestaurantData>

    @DELETE("/user")
    fun withdrawal (@Path("userId") userId: String ) : Call<UserData>

}