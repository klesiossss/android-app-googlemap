package com.consultancyti.gmaps.networkService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.consultancyti.gmaps.model.GoogleMapDTO

class RestApiManager {

        fun sendUserDirection(googleMapDTO: GoogleMapDTO, onResult: (GoogleMapDTO?) -> Unit){
            val retrofit = ServiceBuilder.buildService(RestApi::class.java)
            retrofit.sendDirectionApi(googleMapDTO).enqueue(
                object : Callback<GoogleMapDTO> {
                    override fun onFailure(call: Call<GoogleMapDTO>, t: Throwable) {
                        onResult(null)
                    }
                    override fun onResponse( call: Call<GoogleMapDTO>, response: Response<GoogleMapDTO>) {
                        val serDirection = response.body()
                        onResult(serDirection)
                    }
                }
            )
        }

}