package com.nyenjes.safari.services

import com.nyenjes.safari.model.ServiceType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ServicesService {

    @GET("/service_type")
    fun getServices(): Call<List<ServiceType>>


    @GET("/service_type/{id}")
    fun getServicesById(@Path("id") id: Long): Call<ServiceType>

}
