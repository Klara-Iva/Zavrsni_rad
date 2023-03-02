package com.example.zavrsni_rad.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zavrsni_rad.MainActivity
import com.example.zavrsni_rad.R
import com.example.zavrsni_rad.ui.map.CameraBounds
import com.example.zavrsni_rad.ui.preferences.SavedUserChips
import com.example.zavrsni_rad.ui.rank.SavedStates
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity:AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        SavedStates.setSpinnerIndex(0)
        SavedUserChips.list.clear()
        CameraBounds.camerapostion=
            CameraPosition.fromLatLngZoom(LatLng(45.832995, 17.385692),13.7f)

        val user = Firebase.auth.currentUser
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            setContentView(R.layout.activity_login)
            auth = Firebase.auth
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
            val password = findViewById<EditText>(R.id.editTextTextPassword)
            val loginButton = findViewById<Button>(R.id.loginbuttonmain)
            //login("blabla1@dummy.com","12345qwert")

            loginButton.setOnClickListener {
                if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                    login(email.text.toString(), password.text.toString())
                } else {
                    Toast.makeText(
                        baseContext, "Molimo ispunite sva polja",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            val registerButton = findViewById<Button>(R.id.registerButtonSide)
            registerButton.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Uspješna prijava",Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(baseContext, "Pogrešni email ili lozinka",
                        Toast.LENGTH_SHORT).show()
                    val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
                    val password = findViewById<EditText>(R.id.editTextTextPassword)
                    email.text.clear()
                    password.text.clear()

                }
            }
    }

}