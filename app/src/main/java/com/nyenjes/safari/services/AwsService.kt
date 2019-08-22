package com.nyenjes.safari.services

import com.nyenjes.safari.model.Image
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AwsService {

    @GET("/aws/buckets/bucketName/{bucketName}/folderName/{folderName}")
    fun getContentsInAwsBucket(@Path("bucketName") bucketName: String, @Path("folderName") folderName: String): Call<List<Image>>
}
