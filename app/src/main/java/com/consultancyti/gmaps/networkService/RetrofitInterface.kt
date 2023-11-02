package com.consultancyti.gmaps.networkService


import com.consultancyti.gmaps.model.GoogleMapDTO
import retrofit2.http.*

interface RetrofitInterface {

    @GET(AllApi.URL_DIRECTION)
    suspend fun getDirection(): GoogleMapDTO

}













