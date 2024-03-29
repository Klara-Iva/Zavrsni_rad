package com.example.zavrsni_rad.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.zavrsni_rad.R
import com.example.zavrsni_rad.ui.login.LoginActivity
import com.example.zavrsni_rad.ui.rank.SavedStates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileFragment:Fragment() {
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore
    @SuppressLint("SuspiciousIndentation", "SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onStart()

        val view=inflater.inflate(R.layout.fragment_profile,container, false )


        val greeting = view.findViewById<TextView>(R.id.greetingText)
        user?.let {
            val email = it.email
            val emailText=view.findViewById<TextView>(R.id.UserEmailText)
            emailText.text=email
        }

        user?.let {   db.collection("users")
            .document(it.uid).collection("documents")
            .document("user-info").get()
            .addOnSuccessListener {
                greeting.text = "Pozdrav, "+it.data!!["name"].toString()+"!"
            }
        }

        val logout=view.findViewById<Button>(R.id.logout)
        logout.setOnClickListener{
            SavedStates.setnavigationBarIndex(0)
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            val activity = context as Activity?
            activity!!.finish()
        }

        val resetPasswordButton=view.findViewById<Button>(R.id.resetPasswordButton)
        resetPasswordButton.setOnClickListener{
            FirebaseAuth.getInstance().sendPasswordResetEmail(user?.email.toString())
            Toast.makeText(context,"Provjerite sandučić pošte!",
                Toast.LENGTH_LONG).show()
        }

        val aboutAppText=view.findViewById<TextView>(R.id.aboutApp)
        aboutAppText.setOnClickListener{
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, AboutAppFragment())
            fragmentTransaction?.commit()
        }

        val helpCenter=view.findViewById<TextView>(R.id.helpCenter)
        helpCenter.setOnClickListener{
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, HelpCenter())
            fragmentTransaction?.commit()
        }


        val gradedLocationsCounter=view.findViewById<TextView>(R.id.gradedLocationsCounter)
        val collection = user?.uid?.let { db.collection("users")
            .document(it).collection("documents")}
        val countQuery = collection?.count()
        countQuery?.get(AggregateSource.SERVER)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if(snapshot.count>2){
                    gradedLocationsCounter.text= (snapshot.count-2).toString()
                }
                else{
                    gradedLocationsCounter.text= "0"
                }
            }
        }

        return view
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = user
        if(currentUser == null){
            Toast.makeText(context, "User not found.",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}