package com.example.zavrsni_rad.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.provider.Settings
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.example.zavrsni_rad.LocationDetailsActivity
import com.example.zavrsni_rad.R
import com.example.zavrsni_rad.ui.map.CameraBounds
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val db = Firebase.firestore
    var marker: Marker? = null
    val markers = mutableListOf<Marker?>()
    private val LOCATION_PERMISSION_REQUEST_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraBounds.getCameraPosition()))
        mMap.setOnCameraMoveListener { CameraBounds.setCameraPosition(mMap.cameraPosition) }

        val isNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isNightMode) {
            // Apply the custom dark map style
            mMap.setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_style) })
        }
else{

            mMap.setMapStyle(context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_style_day) })

        }

        // Check for location permission and enable user location
        checkLocationPermission(requireContext())
        mMap.setMyLocationEnabled(true)
        mMap.uiSettings.setMyLocationButtonEnabled(true)
        // Enable zoom controls and compass button
      mMap.uiSettings.setAllGesturesEnabled(true)

// Disable zoom controls only
        mMap.uiSettings.setZoomControlsEnabled(true)

// Disable compass button only
        mMap.uiSettings.setCompassEnabled(true)


        val locations = mutableListOf<MapMarker>()
        val docRef = db.collection("places")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents.documents) {
                    val coordinates = LatLng(
                        document.data!!["latitude"].toString().toDouble(),
                        document.data!!["longitude"].toString().toDouble()
                    )
                    locations.add(MapMarker(document.id, coordinates))
                }
            }
            .addOnCompleteListener {
                for (location in locations) {
                    val myMarker = mMap.addMarker(MarkerOptions().position(location.cordinates))
                    myMarker!!.tag = location.id
                    markers.add(myMarker)
                }

                if (CameraBounds.showSpecifiedLocationOnMap) {
                    marker = mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                CameraBounds.latitude,
                                CameraBounds.longitude
                            )
                        )
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title("Ovdje je!")
                    )
                    marker!!.showInfoWindow()
                    for (mark in markers) {
                        if (marker!!.position == mark?.position)
                            marker!!.tag = mark.tag
                    }
                    mMap.setOnMapClickListener {
                        marker!!.remove()
                    }
                    CameraBounds.showSpecifiedLocationOnMap = false
                }

                mMap.setOnMarkerClickListener {
                    val intent = Intent(context, LocationDetailsActivity::class.java)
                    intent.putExtra("id", it.tag.toString())
                    startActivity(intent)
                    true
                }
            }
    }
        private fun checkLocationPermission(context: Context) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request it
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission granted, show user location
                showUserLocation(mMap)
            }
        }

        private fun showUserLocation(googleMap: GoogleMap) {
            val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager



            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    val marker = MarkerOptions().position(userLatLng).title("You are here")
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    //googleMap.addMarker(marker)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15.0f))
                } else {
                    Toast.makeText(context, "Unable to get your location.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enable GPS to show your location.", Toast.LENGTH_SHORT).show()
            }

        }
    private fun convertAzimuthToDirection(azimuth: Float): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val sector = (azimuth + 22.5f) / 45f
        return directions[(sector.toInt() + 8) % 8]
    }


        data class MapMarker(
            var id: String,
            var cordinates: LatLng
        )

    }
