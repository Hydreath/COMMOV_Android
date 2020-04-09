package com.commov.data.issue

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Issue(
    id: Int,
    var title: String,
    var desc: String,
    var image: String,
    var lat: Double,
    var long: Double
) {

    fun createMarker(): MarkerOptions {
        return MarkerOptions().position(LatLng(this.lat, this.long))
    }

    override fun toString(): String {
        return "Issue(title='$title', lat=$lat, long=$long)"
    }


}