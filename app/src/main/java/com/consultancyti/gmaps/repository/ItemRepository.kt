package com.consultancyti.gmaps.repository


import com.consultancyti.gmaps.model.GoogleMapDTO
import com.consultancyti.gmaps.networkService.RetrofitClient


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class ItemRepository() {

    fun getDirection() : Flow<GoogleMapDTO> = flow {
        val p = RetrofitClient.retrofit.getDirection()
        emit(p)
    }.flowOn(Dispatchers.IO)


}