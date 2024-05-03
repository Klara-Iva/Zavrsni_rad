package com.example.zavrsni_rad.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zavrsni_rad.MainActivity
import com.example.zavrsni_rad.R
import com.example.zavrsni_rad.ui.rank.SavedStates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        val email = findViewById<EditText>(R.id.emailregister)
        val userName = findViewById<EditText>(R.id.yourname)
        val password = findViewById<EditText>(R.id.passwordregister)
        auth = Firebase.auth
        val registerbutton = findViewById<Button>(R.id.registerButtonMain)
        registerbutton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty() && userName.text.isNotEmpty()) {
                if(password.text.length>=8){
                    var str=password.text.toString()
                    var hasNumbers=false
                    var hasLetters=false
                    for(char in str){
                        if(char.isDigit()) hasNumbers=true
                        if(char.isLetter()) hasLetters=true
                    }
                    if(hasNumbers && hasLetters)
                        register(email.text.toString(), password.text.toString(), userName.text.toString())
                    else
                        Toast.makeText(baseContext, "Lozinka ne sadrži barem jedno slovo i barem jedan broj", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(baseContext, "Lozinka mora sadržavati bar 8 znakova", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(baseContext, "Neispravno popunjena polja", Toast.LENGTH_SHORT).show()
        }

        val loginButton = findViewById<Button>(R.id.loginButtonSide)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun register(email: String, password: String, name: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "Račun uspješno kreiran!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    val data = hashMapOf("name" to name)
                    user?.uid?.let {
                        db.collection("users").document(it)
                            .collection("documents")
                            .document("user-info").set(data)
                    }
                    val initalData = hashMapOf(
                        "selectedChips" to "exists"
                    )
                    user?.uid?.let { it1 ->
                        db.collection("users")
                            .document(it1).collection("documents")
                            .document("categoryPreferences")
                            .set(initalData)
                    }
                    val chipselected:ArrayList<String> = arrayListOf()
                    chipselected.add("exists")
                    user?.let {
                        db.collection("users")
                            .document(it.uid).collection("documents")
                            .document("categoryPreferences").update("selectedChips", chipselected)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("registration","true" )
                    SavedStates.setnavigationBarIndex(1)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(baseContext, "Nepoznati error", Toast.LENGTH_SHORT).show()
                }
            }
        return
    }
}
