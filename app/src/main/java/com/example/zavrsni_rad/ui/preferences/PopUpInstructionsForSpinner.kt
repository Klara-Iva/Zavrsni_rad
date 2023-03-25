package com.example.zavrsni_rad.ui.popup

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.example.zavrsni_rad.R
import com.google.firebase.auth.FirebaseAuth

class SpinnerInstructionsPopUp: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.spinner_guide_popup,container, false )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog)


        val btn=view.findViewById<Button>(R.id.dismiss)
        btn.setOnClickListener{
               dismiss()
        }

        }
    }
