package com.example.zavrsni_rad.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.zavrsni_rad.R
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AboutAppFragment:Fragment() {
    private val db = Firebase.firestore


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onStart()
        val view=inflater.inflate(R.layout.fragment_aboutapp,container, false )
        val backButton = view.findViewById<ImageView>(R.id.closeimage)
        backButton?.setOnClickListener {
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, ProfileFragment())
            fragmentTransaction?.commit()
        }
        var aboutapp=view.findViewById<TextView>(R.id.aboutapptext)
        var aboutme=view.findViewById<TextView>(R.id.aboutme)
        db.collection("about").document("aboutapp").get().addOnSuccessListener {
        aboutapp.text=it.data!!["text"].toString()
            aboutme.text=it.data!!["aboutme"].toString()
        }



        return view
    }



}