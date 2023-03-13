package com.example.mymapapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL ="https://maps.googleapis.com"
    val builder = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    val apiservice :Apiservice = builder.create(Apiservice::class.java)


}