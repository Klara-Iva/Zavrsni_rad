package com.example.zavrsni_rad.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.zavrsni_rad.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class HelpCenter:Fragment() {
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onStart()
        val view=inflater.inflate(R.layout.fragment_helpcenter,container, false )
        val sendHelpMessageButton=view.findViewById<Button>(R.id.sendHelpMessage)
        sendHelpMessageButton.setOnClickListener {
            val message=view.findViewById<EditText>(R.id.userInput)
if(!message.text.isNullOrEmpty()){
            user?.let {
                val timevalue = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                    .withZone(ZoneOffset.systemDefault())
                    .format(Instant.now())



                db.collection("helpCenter")
                    .document(timevalue).get()
                    .addOnSuccessListener { it2 ->
                        if (!it2.exists()) {
                            val data = hashMapOf(
                                "exists" to "true"
                            )
                            user.uid.let {
                                    it1 ->   db.collection("helpCenter")
                                .document(timevalue).set(data) }
                        }
                    }
                    .addOnCompleteListener{

                        user?.let {
                            db.collection("helpCenter")
                    .document(timevalue)
                    .update(it.uid,message.text.toString())



                    .addOnSuccessListener {
                        Toast.makeText(context,"Poslano!",Toast.LENGTH_SHORT).show()
                        message.text.clear()
                        context?.let { it1 -> hideSoftKeyboard(view, it1) }
                    }
            }
        }}}
        else{
            Toast.makeText(context,"Nema unosa!",Toast.LENGTH_SHORT).show()
        }
        }

        val backButton = view.findViewById<ImageView>(R.id.closeimage)
        backButton?.setOnClickListener {
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, ProfileFragment())
            fragmentTransaction?.commit()
        }

        return view
    }

    fun Any.hideSoftKeyboard(view: View, context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}