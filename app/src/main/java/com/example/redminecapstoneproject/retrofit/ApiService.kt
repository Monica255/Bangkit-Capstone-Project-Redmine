package com.example.redminecapstoneproject.retrofit

import com.example.redminecapstoneproject.ui.testing.CityResponse
import com.example.redminecapstoneproject.ui.testing.ProvinceResponse
import com.example.redminecapstoneproject.ui.testing.ResponseVerification
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("getprovinsi")
    fun getProvince(): Call<ProvinceResponse>

    @GET("getkota/{id}")
    fun getCities(
        @Path("id") id:Int
    ): Call<CityResponse>


    @Multipart
    @POST("getOcr")
    fun postIDCard(
        @Part("imagefile") imagefile:MultipartBody.Part,
        @Part("uid") uid:String
    ):Call<ResponseVerification>
}