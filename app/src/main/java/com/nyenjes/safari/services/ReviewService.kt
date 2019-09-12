package com.nyenjes.safari.services

import com.nyenjes.safari.model.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewService {

    @GET("/review/place_id/{placeId}")
    fun getReviewsByPlaceId(@Path("placeId") placeId: Long): Call<List<Review>>

    @GET("/review/{id}")
    fun getReviewById(@Path("id") id: Long): Call<Review>

}
