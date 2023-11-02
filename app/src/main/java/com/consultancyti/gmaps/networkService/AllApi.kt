package com.consultancyti.gmaps.networkService

import com.consultancyti.gmaps.utils.toBase64Decode


object AllApi {

    private external fun baseUrlFromJNI(): String

    val BASE_URL = baseUrlFromJNI().toBase64Decode()
// api/demo/jsondemoapi.php?option=3
    private const val V1 = "api/demo/"

    const val URL_DIRECTION = "https://maps.googleapis.com/maps/api/directions/json?destination=Montreal&origin=Toronto&key=AIzaSyC56ABjgbnY17a-okHWm_hhaYCLXhEyHDQ"


//    https://touhidapps.com/api/demo/jsondemoapi.php?option=3

}
