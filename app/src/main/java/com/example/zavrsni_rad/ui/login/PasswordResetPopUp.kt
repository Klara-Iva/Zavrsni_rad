package com.example.zavrsni_rad.ui.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.example.zavrsni_rad.R
import com.google.firebase.auth.FirebaseAuth

class PasswordResetPopUp: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.reset_password_popup,container, false )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text=view.findViewById<EditText>(R.id.emailAdress)
        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog);
        val sendEmailButton=view.findViewById<Button>(R.id.sendEmail)
        sendEmailButton.setOnClickListener{
            if(text.text.toString().isNotEmpty()){
                FirebaseAuth.getInstance().sendPasswordResetEmail(text.text.toString())
                Toast.makeText(context,"Provjerite sandučić pošte!",
                    Toast.LENGTH_LONG).show()
                dismiss()
            }
            else{
                Toast.makeText(context,"Unesite email adresu.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}