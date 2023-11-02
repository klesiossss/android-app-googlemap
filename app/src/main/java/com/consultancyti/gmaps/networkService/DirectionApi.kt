package com.consultancyti.gmaps.networkService

import com.consultancyti.gmaps.model.GoogleMapDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface DirectionApi {
    @GET("json")
    suspend fun getDirectionApi(@Query("origin") origin:String,@Query("destination") destination:String, @Query("key") key:String) : Response<GoogleMapDTO>

    @Headers("Content-Type: application/json")
    @POST("sendUserCoordenadas")
    suspend fun sendDirectionApi( @Body googleMapDTO: GoogleMapDTO)

}