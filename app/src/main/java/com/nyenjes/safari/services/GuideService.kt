package com.nyenjes.safari.services

import com.nyenjes.safari.model.Guide
import com.nyenjes.safari.model.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GuideService {

    @GET("/agent")
    fun  getGuides(): Call<List<Guide>>

    @GET("/agent/{id}")
    fun getGuideById(@Path("id") id: Long): Call<Guide>
}
