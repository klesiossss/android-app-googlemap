package com.consultancyti.gmaps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.consultancyti.gmaps.databinding.ActivityMainBinding
import com.consultancyti.gmaps.model.Bounds
import com.consultancyti.gmaps.model.GoogleMapDTO
import com.consultancyti.gmaps.networkService.DirectionApi
import com.consultancyti.gmaps.networkService.RestApiManager
import com.consultancyti.gmaps.networkService.RetrofitHelper
import com.consultancyti.gmaps.networkService.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.data.Renderer
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private val interval: Long = 10000 // 10seconds
    private val fastestInterval: Long = 5000 // 5 seconds
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val requestPermissionCode = 999
    private val result = ArrayList<List<LatLng>>()
    val originDest = ArrayList<LatLng>()
    private lateinit var userLatLng : LatLng
    private lateinit var data: GoogleMapDTO

    private val places = arrayListOf(
        Place(
            "Santa Clara",
            //  LatLng(-2.5076262, -44.3013876),
            //LatLng(43.7845791, -79.4436018),
            LatLng(37.3647762,-121.9888087),
            // "Av. Cel. Colares Moreira, 16 - Jardim Renascença, São Luís - MA, 65075-441",
            "2788 San Tomas Expy, Santa Clara, CA 95051, United States",
            4.5f
        ),
        Place(
            "Round Table Pizza",
            //   LatLng(-2.5245532, -44.2521681),

            //LatLng(43.681697, -79.6171045),
            LatLng(37.3713225,-122.0415163),
            //"Av. Daniel de La Touche, 51 - Maranhão Novo, São Luís - MA, 65000-000",
            "860 Old San Francisco Rd, Sunnyvale, CA 94086, United States",
            4.5f
        )
    )

    private var mLatitudeTextView: TextView? = null
    private var mLongitudeTextView: TextView? = null

    //@RequiresApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment


        //evolution
     /*   binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = LocationRequest.create()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertMessage()
        }

        //evolution end

        mLatitudeTextView = findViewById(R.id.latitudeText) as TextView
        mLongitudeTextView = findViewById(R.id.longitudeText) as TextView


    //getLastKnownLocation(mapFragment,this)

        mapFragment.getMapAsync { googleMap ->
 /*           checkForPermission(this)
            startLocationUpdates()*/



            lifecycleScope.launch {
                 apiCall(googleMap)
                val origin = LatLng(data.routes.get(0).legs.get(0).startLocation.lat, data.routes.get(0).legs.get(0).startLocation.lng)
                val dest =  LatLng(data.routes.get(0).legs.get(0).endLocation.lat,data.routes.get(0).legs.get(0).endLocation.lng)
                val arrayPlace = arrayListOf(origin,dest)
                originDest.add(origin)
                originDest.add(dest)
                addMarker(googleMap,arrayPlace)
                //sendCoordenadasUser(data)

            }

            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()
                originDest.forEach {
                    bounds.include(it)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
                    //circle
                    googleMap.addCircle(
                        CircleOptions()
                            .center(it)
                            .radius(100.0)
                            .fillColor(Color.TRANSPARENT)
                            .strokeColor(Color.DKGRAY)
                    )

                }



                //bound for location user
                 userLatLng = LatLng(latitude,longitude)
                //       val boundsUserLocation = LatLngBounds.builder()
                //     boundsUserLocation.include(userLatLng)
               // bounds.include(userLatLng)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16f))
                //circle
                googleMap.addCircle(
                    CircleOptions()
                        .center(userLatLng)
                        .radius(1000.0)
                        .fillColor(Color.TRANSPARENT)
                        .strokeColor(Color.DKGRAY)
                )

                val mark = googleMap.addMarker(
                    MarkerOptions()
                        .title("localUser")
                        .snippet("local")
                        .position(userLatLng)
                        .icon(
                            BitMapHelper.vectorToBitmp(
                                this, R.drawable.ic_android_black_24dp, ContextCompat.getColor(
                                    this,
                                    androidx.appcompat.R.color.background_material_dark
                                )
                            )
                        )
                )


                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),100))

            }



        }

    }

    private suspend fun sendCoordenadasUser(coordenadas:GoogleMapDTO){
        try {
            val sendDirectionUserCoordenadas =
                RetrofitHelper.getInstanceBackEnd().create(DirectionApi::class.java)
            sendDirectionUserCoordenadas.sendDirectionApi(coordenadas)
        }catch (e: RuntimeException){
            e.printStackTrace()
        }

    }


    private suspend fun apiCall(googleMap: GoogleMap) {
        val directionApi = RetrofitHelper.getInstance().create(DirectionApi::class.java)
        // launching a new coroutine
        val origin = "Rua Domingos Almeida 323 Centro Humberto de Campos MA"
            //places.get(0).addres
        val destination = "Rua Irineu Santos Centro Humberto de Campos "
            //places.get(1).addres
        val key = "AIzaSyC56ABjgbnY17a-okHWm_hhaYCLXhEyHDQ"
        val result = directionApi.getDirectionApi(origin, destination, getString(R.string.api_key))

        // Checking the results
            Log.d("ayush: ", result.body().toString())
         data = result.body()!!
        if (data != null) {
            callingApiDirection(data, googleMap)
        }
    }

    //sao luis lat -2.530376  lng -44.3042442
    //hc lat -2.595826  lng -43.464965


    private fun addMarker(googleMap: GoogleMap,placesList: List<LatLng>) {
        Log.i("AddMark", "Inicio marking")
        placesList.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(place)
                    .icon(
                        BitMapHelper.vectorToBitmp(
                            this, R.drawable.ic_android_black_24dp, ContextCompat.getColor(
                                this,
                                androidx.appcompat.R.color.background_material_dark
                            )
                        )
                    )
            )
        }


    }

    public suspend fun callingApiDirection(respObj: GoogleMapDTO, googleMap: GoogleMap) {
        //val result = ArrayList<List<LatLng>>()
        try {
            // val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)
            val path = ArrayList<LatLng>()
            for (i in 0..(respObj.routes[0].legs[0].steps.size - 1)) {
                path.addAll(decodePoly(respObj.routes[0].legs[0].steps[i].polyline.points))
            }
            result.add(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val lineOption = PolylineOptions()
        for (i in result.indices) {
            lineOption.addAll(result[i])
            lineOption.width(10f)
            lineOption.color(Color.BLUE)
            lineOption.geodesic(true)
        }
        googleMap.addPolyline(lineOption)
    }


    private fun decodePoly(encoded: String): ArrayList<LatLng> {
        Log.i("Location", "String received: $encoded")
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lng += dlng
            val p = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(p)
        }
        for (i in 0 until poly.size) {
            Log.i(
                "Location",
                "Point sent: Latitude: ${poly[i].latitude} Longitude: ${poly[i].longitude}"
            )
        }
        return poly
    }




    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }



override fun onPause() {
    super.onPause()
    fusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
}

private val mLocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        locationResult.lastLocation
        Log.d("MainActivity", "callback: $latitude $longitude")
        locationChanged(locationResult.lastLocation)
        latitude = locationResult.lastLocation.latitude
        longitude = locationResult.lastLocation.longitude
        mLongitudeTextView!!.text = mLastLocation.longitude.toString()
        mLatitudeTextView!!.text = mLastLocation.latitude.toString()

    }
}

private fun startLocationUpdates() {
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = interval
    mLocationRequest.fastestInterval = fastestInterval

    val builder = LocationSettingsRequest.Builder()
    builder.addLocationRequest(mLocationRequest)
    val locationSettingsRequest = builder.build()
    val settingsClient = LocationServices.getSettingsClient(this)
    settingsClient.checkLocationSettings(locationSettingsRequest)

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    if (ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
        return
    }
    fusedLocationProviderClient!!.requestLocationUpdates(
        mLocationRequest,
        mLocationCallback,
        Looper.myLooper()!!)
}

@RequiresApi(Build.VERSION_CODES.M)
private fun checkForPermission(context: Context) {
    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
        return
    } else {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            requestPermissionCode)
        return
    }
}

private fun showAlertMessage() {
    val builder = AlertDialog.Builder(this)
    builder.setMessage("The location permission is disabled. Do you want to enable it?")
        .setCancelable(false)
        .setPositiveButton("Yes") { _, _ ->
            startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                , 10)
        }
        .setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
            finish()
        }
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun locationChanged(location: Location) {
    mLastLocation = location
    longitude = mLastLocation.longitude
    latitude = mLastLocation.latitude
    mLongitudeTextView!!.text = mLastLocation.longitude.toString()
    mLatitudeTextView!!.text = mLastLocation.latitude.toString()
    Log.d("MainActivity", "function: $latitude $longitude")
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == requestPermissionCode) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}


}





data class Place(
    val name: String,
    val latLng:LatLng,
    val addres:String,
    val  rating:Float

)
///https://github.com/topics/google-maps-android?o=desc&s=updated
