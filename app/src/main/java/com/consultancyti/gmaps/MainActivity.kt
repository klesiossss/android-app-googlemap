package com.consultancyti.gmaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {
    private val places = arrayListOf(
        Place("Ocular",LatLng(-2.5076262,-44.3013876),"Av. Cel. Colares Moreira, 16 - Jardim Renascença, São Luís - MA, 65075-441",4.5f),
        Place("St. Louis Adventist College",LatLng(-2.5245532,-44.2521681),"Av. Daniel de La Touche, 51 - Maranhão Novo, São Luís - MA, 65000-000",4.5f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync{googleMap->
            addMarker(googleMap)

            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()
                places.forEach{
                    bounds.include(it.latLng)
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),100))
            }
        }
    }

    private fun addMarker(googleMap:GoogleMap){
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.addres)
                    .position(place.latLng)
                    .icon(BitMapHelper.vectorToBitmp(this,R.drawable.ic_android_black_24dp,ContextCompat.getColor(this,
                        androidx.appcompat.R.color.background_material_dark)))
            )
        }
    }
}

data class Place(
    val name: String,
    val latLng:LatLng,
    val addres:String,
    val  rating:Float

)