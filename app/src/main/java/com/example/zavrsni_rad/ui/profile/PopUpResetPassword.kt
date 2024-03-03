package com.example.zavrsni_rad.ui.profile
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PopUpResetPassword :DialogFragment() {
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.reset_password_popup,container, false )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog);
        val sendEmailButton=view.findViewById<Button>(R.id.sendEmail)
        sendEmailButton.setOnClickListener {

            user?.email?.let { it1 -> FirebaseAuth.getInstance().sendPasswordResetEmail(it1) }
            Toast.makeText(
                context, "Provjerite sandučić pošte!",
                Toast.LENGTH_LONG
            ).show()
        }
            val dont_send=view.findViewById<Button>(R.id.dontSendEmail)
            dont_send.setOnClickListener {
                dismiss()
            }
        }
    }

