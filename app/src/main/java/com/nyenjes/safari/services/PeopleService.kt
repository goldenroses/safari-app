package com.nyenjes.safari.services

import com.nyenjes.safari.model.Place
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PeopleService {

    @GET("/person")
    fun  getPlaces(): Call<List<Place>>

    @GET("/place/{id}")
    fun getPlaceById(@Path("id") id: Long): Call<Place>
}
