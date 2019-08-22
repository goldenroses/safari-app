package com.nyenjes.safari.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {
    companion object {
        val URL: String = "http://safari-app.herokuapp.com"

        var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(interceptor)

        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttp.build())
        val retro: Retrofit = retrofitBuilder.build()


        fun <S> buildService(serviceType: Class<S>): S {
            return retro.create(serviceType)

        }
    }
}