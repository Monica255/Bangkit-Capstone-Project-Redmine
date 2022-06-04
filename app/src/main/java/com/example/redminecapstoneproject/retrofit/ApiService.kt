package com.example.redminecapstoneproject.retrofit

import com.example.redminecapstoneproject.ui.testing.CityResponse
import com.example.redminecapstoneproject.ui.testing.ProvinceResponse
import com.example.redminecapstoneproject.ui.testing.ResponseOtp
import com.example.redminecapstoneproject.ui.testing.ResponseVerification
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("getprovinsi")
    fun getProvince(): Call<ProvinceResponse>

    @GET("getkota/{id}")
    fun getCities(
        @Path("id") id:Int
    ): Call<CityResponse>





}

interface ApiService2{
    //@Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST("getOcr")
    fun postIDCard(
        @Part imagefile :MultipartBody.Part,
        @Part("uid") uid:RequestBody
    ):Call<ResponseVerification>

    @Multipart
    @POST("sendOtp")
    fun sendOtp(
        @Part("email") email:RequestBody
    ):Call<ResponseOtp>

}