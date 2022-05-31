package com.example.redminecapstoneproject.retrofit

import com.example.redminecapstoneproject.ui.testing.CityResponse
import com.example.redminecapstoneproject.ui.testing.ProvinceResponse
import com.example.redminecapstoneproject.ui.testing.ResponseMsg
import com.example.redminecapstoneproject.ui.testing.ResponseVerification
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTlzc1ZKVjY4ZDlHLXcycEYiLCJpYXQiOjE2NTM5OTY4NjR9.sbB_y4YvHA2i2Oe6CWZgJso99BZ02LJH9s6ve5x6U2o"
    ): Call<ResponseMsg>

}