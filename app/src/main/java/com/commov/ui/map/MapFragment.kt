package com.commov.ui.map

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.commov.R
import com.commov.data.issue.Issue
import com.commov.network.IssueFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var currentLocation: LatLng = LatLng(.0, .0)
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var currentPhotoView: ImageView
    private lateinit var currentImage: Bitmap
    private lateinit var map: GoogleMap
    private val markers: HashMap<Marker, Issue> = HashMap()

    private var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            currentLocation = LatLng(location!!.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.title = "Map"

        locationManager =
            (activity as AppCompatActivity).getSystemService(LOCATION_SERVICE) as LocationManager

        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup create issue pop up
        val fab: FloatingActionButton = view.findViewById(R.id.addIssue)
        fab.setOnClickListener { view ->
            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.popup_issue_creator, null)
            val builder = AlertDialog.Builder(context!!).setView(dialogView).setTitle("Add Issue")
            val alertDialog = builder.show()
            alertDialog.setCanceledOnTouchOutside(true)
            this.currentPhotoView = dialogView.findViewById<ImageView>(R.id.issuePhoto)
            this.currentPhotoView.setOnClickListener {
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 123)
            }

            val btn = dialogView.findViewById<Button>(R.id.saveIssue)
            btn.setOnClickListener {
                val title = dialogView.findViewById<EditText>(R.id.issueTitleEdit).text.toString()
                val desc = dialogView.findViewById<EditText>(R.id.issueDescEdit).text.toString()
                //val photo = bitmapToBase64(this.currentImage)

                IssueFactory.createIssue(
                    context!!,
                    title,
                    desc,
                    currentLocation.latitude.toFloat(),
                    currentLocation.longitude.toFloat(),
                    null
                ) {
                    this.map.addMarker(MarkerOptions().position(currentLocation))
                    // ADD LOGIC AFTER
                }
            }


        }

        //ask for permissions for gps
        if (ActivityCompat.checkSelfPermission(
                this.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            }
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            this.locationListener
        )

        mapView = view.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            this.map = p0
        }
        p0?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentLocation, 12.0f
            )
        )

        p0!!.setOnMarkerClickListener(this)

        // INIT ALL POINTS SOON
        IssueFactory.getAllIssues(this.context!!){
            initMarkers(p0, it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_DENIED -> {
                }
                PackageManager.PERMISSION_GRANTED -> {
                    this.findNavController().navigate(R.id.mapFragment)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            this.currentImage = data?.extras?.get("data") as Bitmap
            this.currentPhotoView.setImageBitmap(this.currentImage)
        }
    }


    private fun bitmapToBase64(image: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        val byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private fun initMarkers(p0: GoogleMap?, response: JSONObject){
        println(response.get("data"))
        val array = response.get("data") as JSONArray
        for(i in 0 until array.length()){
            val temp = array.getJSONObject(i)
            val iss = Issue(temp.getInt("issueId"), temp.getString("title"), temp.getString("description"), temp.getString("imagePath"), temp.getDouble("lat"), temp.getDouble("long"))
            val marker = p0?.addMarker(iss.createMarker())
            markers.put(marker!!, iss)
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        println(markers.get(p0!!).toString())
        return true;
    }
}
