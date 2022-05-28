package com.example.redminecapstoneproject.retrofit

import com.example.redminecapstoneproject.ui.testing.CityResponse
import com.example.redminecapstoneproject.ui.testing.ProvinceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("getprovinsi")
    fun getProvince(): Call<ProvinceResponse>

    @GET("getkota/{id}")
    fun getCities(
        @Path("id") id:Int
    ): Call<CityResponse>
}