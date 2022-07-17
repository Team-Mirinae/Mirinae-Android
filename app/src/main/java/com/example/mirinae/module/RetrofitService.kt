package com.example.mirinae.module

import com.example.mirinae.module.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService
{
    @POST("/user/sign-in")
    fun login(@Field("id") id : String,
              @Field("pw") pw : String) : Call<UserRes>

    @POST("/user/sign-up")
    fun signUp(@Field("id") id : String,
               @Field("pw") pw : String,
               @Field("name") name : String
               ) : Call<UserRes>

    @POST("/restaurant/save")
    fun saveRestaurant(@Field("title") title : String,
                      @Field("content") content : String,
                      @Field("latitude") latitude : Double,
                      @Field("longitude") longitude : Double) : Call<SaveRestaurantRes>

    @GET("/restaurant/favorite")
    fun getAllRestaurant(@Path("userId") userId : String) : Call<getAllRestaurantRes>

    @GET("/restaurant/search")
    fun searchRestaurant(@Query("title") title : String) : Call<getRestaurantRes>

    @DELETE("/restaurant/favorite")
    fun deleteRestaurant(@Path("title") title : String) : Call<DeleteRes>

    @DELETE("/user")
    fun withdrawal (@Path("userId") userId: String ) : Call<DeleteRes>

}