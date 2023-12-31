package com.consultancyti.gmaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitMapHelper {
   fun vectorToBitmp(
       context:Context,
       @DrawableRes id: Int,
       @ColorInt color:Int,

   ) : BitmapDescriptor{
       val vectorDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
       return if (vectorDrawable == null) {
           BitmapDescriptorFactory.defaultMarker()
       } else {
           val bitmap = Bitmap.createBitmap(
               vectorDrawable.intrinsicWidth,
               vectorDrawable.intrinsicHeight,
               Bitmap.Config.ARGB_8888
           )
           val canvas = Canvas(bitmap)
           vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)

           DrawableCompat.setTint(vectorDrawable, color)
           vectorDrawable.draw(canvas)
           return BitmapDescriptorFactory.fromBitmap(bitmap)
       }
   }
}