package com.commov.data.issue

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Issue(
    id: Int,
    var title: String,
    var desc: String,
    var image: String,
    var lat: Double,
    var long: Double,
    var date:String
) {

    fun createMarker(): MarkerOptions {
        return MarkerOptions().position(LatLng(this.lat, this.long))
    }

    override fun toString(): String {
        return "Issue(title='$title', lat=$lat, long=$long)"
    }

    fun imageToBitmap(): Bitmap {
        val decodedString: ByteArray = Base64.decode(this.image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun getDateFormated(): String {
        return date.substring(0,10)
    }
}