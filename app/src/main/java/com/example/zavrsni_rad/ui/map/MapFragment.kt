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


class MapFragment:Fragment(), OnMapReadyCallback {
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

       /*var i:Int=10

        val initalData = hashMapOf(

            "originalitySum" to 0.0,
            "originalityCount" to 0,
            "originalityAverage" to 0.0,
            "excitementSum" to 0.0,
            "excitementCount" to 0,
            "excitementAverage" to  0.0,
            "accessibilitySum"  to 0.0,
            "accessibilityCount" to 0,
            "accessibilityAverage" to 0.0,
            "photogenicSum" to 0.0,
            "photogenicCount" to 0,
            "photogenicAverage" to 0.0,
            "timeWorthSum" to 0.0,
            "timeWorthCount" to  0,
            "timeWorthAverage" to 0.0,
        )
        db.collection("places").document("01").update(initalData as Map<String, Any>)
        db.collection("places").document("02").update(initalData as Map<String, Any>)
        db.collection("places").document("03").update(initalData as Map<String, Any>)
        db.collection("places").document("04").update(initalData as Map<String, Any>)
        db.collection("places").document("05").update(initalData as Map<String, Any>)
        db.collection("places").document("06").update(initalData as Map<String, Any>)
        db.collection("places").document("07").update(initalData as Map<String, Any>)
        db.collection("places").document("08").update(initalData as Map<String, Any>)
        db.collection("places").document("09").update(initalData as Map<String, Any>)
        while(i<=29) {
            db.collection("places").document(i.toString()).update(initalData as Map<String, Any>)

            i++
        }*/




    }


    data class MapMarker(
        var id: String,
        var cordinates: LatLng
    )

}