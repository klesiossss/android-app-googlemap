package com.consultancyti.gmaps.networkService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val baseUrl = "https://maps.googleapis.com/maps/api/directions/"
    val baseUrlBackEnd = "http://192.168.1.33:8080/v1/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

    fun getInstanceBackEnd(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrlBackEnd)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}