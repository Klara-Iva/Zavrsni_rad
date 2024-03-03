package com.example.zavrsni_rad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class AddNewLocationActivity: AppCompatActivity() {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private val storage = Firebase.storage
    private var id: Int= 0
    val PICK_IMAGE_REQUEST = 1
    val PICK_IMAGE_REQUEST2 = 2
    public final lateinit var selectedImageUri: Uri
    public final lateinit var selectedImageUriSmall: Uri

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_location)

        val backButton = findViewById<ImageView>(R.id.closebutton)
        backButton?.setOnClickListener {
            finish()
        }

         val selectedChipsTextArray: ArrayList<String> = arrayListOf()
         val chipGroup = findViewById<ChipGroup>(R.id.addChipGroupCategories)
         chipGroup.setOnCheckedStateChangeListener { group, _ ->
             val chipsId = group.checkedChipIds
             selectedChipsTextArray.clear()
             for (ids in chipsId) {
                 val chip = group.findViewById<Chip>(ids!!)
                 selectedChipsTextArray.add(chip.text.toString())
             }
         }


        val chooseImageButton = findViewById<Button>(R.id.chooseBigPicture)
        val selectedImageView = findViewById<ImageView>(R.id.selectedImageViewBig)

        chooseImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Odaberi sliku"),
                PICK_IMAGE_REQUEST
            )
        }

        val chooseImageButtonSmall = findViewById<Button>(R.id.chooseSmallPicture)
        val selectedImageViewSmall = findViewById<ImageView>(R.id.selectedImageViewSmall)

        chooseImageButtonSmall.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Odaberi sliku"),
                PICK_IMAGE_REQUEST2
            )
        }


        val saveButton = findViewById<Button>(R.id.saveNewLocation)




        // Kada se odabere slika, prikaži je u ImageView
        selectedImageUri = Uri.parse("android.resource://$packageName/drawable/logo")
        selectedImageView.setImageURI(selectedImageUri)
        selectedImageView.visibility = ImageView.GONE

        selectedImageUriSmall = Uri.parse("android.resource://$packageName/drawable/logo")
        selectedImageViewSmall.setImageURI(selectedImageUri)
        selectedImageViewSmall.visibility = ImageView.GONE

        saveButton.setOnClickListener {

            val name = findViewById<TextView>(R.id.locationName)
            val longitude = findViewById<TextView>(R.id.addLongitude)
            val latitude = findViewById<TextView>(R.id.addLatitude)
            val description = findViewById<TextView>(R.id.addDescription)
            val link = findViewById<TextView>(R.id.addLink)



            if (name.text != null && longitude.text != null && latitude.text != null && description.text != null) {
                val uid = user?.uid
                val collection = db.collection("places")
                collection.get()
                    .addOnSuccessListener { querySnapshot ->
                        id = querySnapshot.size() + 1

                        val initialData = hashMapOf(
                            "description" to description.text.toString(),
                            "name" to name.text.toString(),
                            "longitude" to longitude.text.toString().toDouble(),
                            "latitude" to latitude.text.toString().toDouble(),
                            "originalitySum" to 0.0,
                            "originalityCount" to 0,
                            "originalityAverage" to 0.0,
                            "excitementSum" to 0.0,
                            "excitementCount" to 0,
                            "excitementAverage" to 0.0,
                            "accessibilitySum" to 0.0,
                            "accessibilityCount" to 0,
                            "accessibilityAverage" to 0.0,
                            "photogenicSum" to 0.0,
                            "photogenicCount" to 0,
                            "photogenicAverage" to 0.0,
                            "timeWorthSum" to 0.0,
                            "timeWorthCount" to 0,
                            "timeWorthAverage" to 0.0,
                            "category" to selectedChipsTextArray,
                            "addedByUser" to uid.toString()
                        )

                        if (link.text.isNotEmpty()) {
                            initialData["link"] = link.text.toString()
                        }


                        val timevalue = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                        DateTimeFormatter
                            .ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                            .withZone(ZoneOffset.systemDefault())
                            .format(Instant.now())
                        val imagename = user?.uid + "  " + timevalue.toString()+"1"
                        val imagename2 = user?.uid + "  " + timevalue.toString()+"2"

                        var storageRef = storage.reference.child("uploadedLocationImages/$imagename")
                        val selectedImageUri: Uri = selectedImageUri
                        val selectedImageUri2: Uri = selectedImageUriSmall

                        val uploadTask: UploadTask = storageRef.putFile(selectedImageUri)
                        uploadTask.addOnSuccessListener { _ ->
                            Toast.makeText(this, "Picture big uploaded!", Toast.LENGTH_SHORT).show()
                        }.addOnCompleteListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUrl = uri.toString()

                                db.collection("places")
                                    .document(id.toString()).update("image1",downloadUrl)


                        storageRef = storage.reference.child("uploadedLocationImages/$imagename2")
                        val uploadTask2: UploadTask = storageRef.putFile(selectedImageUri2)
                        uploadTask2.addOnSuccessListener { _ ->
                            Toast.makeText(this, "Picture small uploaded!", Toast.LENGTH_SHORT)
                                .show()

                        }.addOnCompleteListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUrl2 = uri.toString()

                                db.collection("places")
                                    .document(id.toString()).update("image2",downloadUrl2)

                            }

                        }
                            }

                        }

                        db.collection("places")
                            .document(id.toString())
                            .set(initialData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Spremljeno!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            this.selectedImageUri = data.data!!
            val selectedImageView = findViewById<ImageView>(R.id.selectedImageViewBig)
            selectedImageView.setImageURI(selectedImageUri)
            selectedImageView.visibility = ImageView.VISIBLE
        }


        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            this.selectedImageUriSmall = data.data!!
            val selectedImageView = findViewById<ImageView>(R.id.selectedImageViewSmall)
            selectedImageView.setImageURI(selectedImageUriSmall)
            selectedImageView.visibility = ImageView.VISIBLE
        }

    }






}
