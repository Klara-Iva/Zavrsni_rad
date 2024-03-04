package com.example.zavrsni_rad.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.zavrsni_rad.LocationDetailsActivity
import com.example.zavrsni_rad.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MMapFragment:Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val db = Firebase.firestore
    var marker: Marker? = null
    val markers = mutableListOf<Marker?>()

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
        mMap.setOnCameraMoveListener { CameraBounds.setCameraPosition( mMap.cameraPosition) }
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
            .addOnCompleteListener{
                for (location in locations) {
                    val myMarker = mMap.addMarker(MarkerOptions().position(location.cordinates))
                    myMarker!!.tag = location.id
                    markers.add(myMarker)
                }

                if(CameraBounds.showSpecifiedLocationOnMap) {
                    marker = mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                CameraBounds.latitude,
                                CameraBounds.longitude
                            )
                        ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
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
                    CameraBounds.showSpecifiedLocationOnMap=false
                }

                mMap.setOnMarkerClickListener {
                    val intent = Intent(context, LocationDetailsActivity::class.java)
                    intent.putExtra("id", it.tag.toString())
                    startActivity(intent)
                    true
                }
            }
    }


    data class MapMarker(
        var id: String,
        var cordinates: LatLng
    )

}