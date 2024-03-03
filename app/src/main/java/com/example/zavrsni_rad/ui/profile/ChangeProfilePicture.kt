package com.example.zavrsni_rad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

class ChangeProfilePicture : AppCompatActivity() {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private val storage = Firebase.storage
    val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.fragment_change_profile_picture)


        val choose = findViewById<Button>(R.id.chooseNewProfilePicture)

        val image = findViewById<ImageView>(R.id.chosenUserProfilePicture)

        choose.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Odaberi sliku"),
                PICK_IMAGE_REQUEST
            )
        }
        val upload = findViewById<Button>(R.id.uploadPicture)
        selectedImageUri =Uri.parse("android.resource://${packageName}.packageName}/${R.drawable.ic_baseline_person_24}")
        image.setImageURI(selectedImageUri)
        image.visibility = ImageView.VISIBLE



        upload.setOnClickListener {
            Toast.makeText(this, "Pričekajte!", Toast.LENGTH_SHORT).show()
            val imageName = user?.uid + "-profilePicture"
            var storageRef = storage.reference.child("UsersProfilePictures/$imageName")
            val selectedImageUri: Uri = selectedImageUri
            val uploadTask: UploadTask = storageRef.putFile(selectedImageUri)
            uploadTask.addOnSuccessListener { _ ->
                Toast.makeText(this, "Picture uploaded!", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()

                    db.collection("users")
                        .document(user!!.uid).collection("documents")
                        .document("user-info").update("profilePicture", downloadUrl)
                        .addOnCompleteListener{
                            finish()
                        }

                }
            }
        }

        val backButton = findViewById<ImageView>(R.id.closeimagechoosing)
        backButton?.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            this.selectedImageUri = data.data!!
            val selectedImageView = findViewById<ImageView>(R.id.chosenUserProfilePicture)
            selectedImageView?.setImageURI(selectedImageUri)
            selectedImageView?.visibility = ImageView.VISIBLE
        }
    }


}
