package com.consultancyti.gmaps.networkService

import com.consultancyti.gmaps.model.GoogleMapDTO

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("sendUserCoordenadas")
    fun sendDirectionApi( @Body googleMapDTO: GoogleMapDTO): Call<GoogleMapDTO>
}