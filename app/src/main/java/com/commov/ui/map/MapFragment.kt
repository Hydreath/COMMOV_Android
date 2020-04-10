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
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.commov.MainActivity
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

    private var currentLocation: LatLng = LatLng(41.709315, -8.825261)
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var currentPhotoView: ImageView
    private lateinit var currentImage: Bitmap
    private var map: GoogleMap? = null
    private val markers: HashMap<Marker, Issue> = HashMap()

    private var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            currentLocation = LatLng(location!!.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.map)

        locationManager =
            (activity as AppCompatActivity).getSystemService(LOCATION_SERVICE) as LocationManager

        return inflater.inflate(R.layout.fragment_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup create issue pop up
        view.findViewById<FloatingActionButton>(R.id.center).setOnClickListener {
            map?.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        }

        val fab: FloatingActionButton = view.findViewById(R.id.addIssue)
        fab.setOnClickListener { view ->
            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.popup_issue_creator, null)
            val builder = AlertDialog.Builder(context!!).setView(dialogView).setTitle("Add Issue")
            val alertDialog = builder.show()
            alertDialog.setCanceledOnTouchOutside(true)
            this.currentPhotoView = dialogView.findViewById<ImageView>(R.id.rIssuePhoto)
            this.currentPhotoView.setOnClickListener {
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 123)
            }

            val btn = dialogView.findViewById<Button>(R.id.saveIssue)
            btn.setOnClickListener {
                val title = dialogView.findViewById<EditText>(R.id.issueTitleEdit).text.toString()
                val desc = dialogView.findViewById<EditText>(R.id.issueDescEdit).text.toString()
                val photo = bitmapToBase64(this.currentImage)
                if (title.isNotEmpty() && desc.isNotEmpty() && photo.isNotEmpty()) {
                    IssueFactory.createIssue(
                        context!!,
                        title,
                        desc,
                        currentLocation.latitude.toFloat(),
                        currentLocation.longitude.toFloat(),
                        photo
                    ) {
                        this.map?.addMarker(MarkerOptions().position(currentLocation))
                        // ADD LOGIC AFTER
                        alertDialog.dismiss()
                        Toast.makeText(context, getString(R.string.issueCreated), Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.createIssueFieldsMissing),
                        Toast.LENGTH_LONG
                    ).show()
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

        IssueFactory.getAllIssues(this.context!!) {
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

    private fun initMarkers(p0: GoogleMap?, response: JSONObject) {
        println(response.get("data"))
        val array = response.get("data") as JSONArray
        for (i in 0 until array.length()) {
            val temp = array.getJSONObject(i)
            val iss = Issue(
                temp.getInt("issueId"),
                temp.getString("title"),
                temp.getString("description"),
                temp.getString("imagePath"),
                temp.getDouble("lat"),
                temp.getDouble("long"),
                temp.getString("createdAt")
            )
            val marker = p0?.addMarker(iss.createMarker())
            markers.put(marker!!, iss)
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        markers.get(p0!!)?.let { createMarkerPopup(it) }
        return true;
    }

    fun createMarkerPopup(issue: Issue) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.popup_read_issue, null)
        val builder = AlertDialog.Builder(context!!).setView(dialogView).setTitle(issue.title)
        val alertDialog = builder.show()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.findViewById<TextView>(R.id.rIssueDescription)?.text = issue.desc
        alertDialog.findViewById<TextView>(R.id.rIssueDate)?.text = issue.getDateFormated()
        alertDialog.findViewById<ImageView>(R.id.rImageIssue)?.setImageBitmap(issue.imageToBitmap())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                (activity as MainActivity).openDrawer()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
