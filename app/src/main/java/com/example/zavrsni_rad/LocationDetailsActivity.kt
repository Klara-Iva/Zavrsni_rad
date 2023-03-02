package com.example.zavrsni_rad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.bumptech.glide.Glide
import com.example.zavrsni_rad.ui.map.CameraBounds
import com.example.zavrsni_rad.ui.rank.SavedStates
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat



class LocationDetailsActivity: AppCompatActivity() {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    var TimeWorthButtoIsChecked:Boolean= false
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_location_details)
        val id=intent.getStringExtra("id")
        val name = findViewById<TextView>(R.id.name)
        val description = findViewById<TextView>(R.id.description)
        val image1 = findViewById<ImageView>(R.id.image1)
        val image2 = findViewById<ImageView>(R.id.image2)
         val docRef = db.collection("places").document(id!!)
        docRef.get()
            .addOnSuccessListener { document ->
                name.text = document?.data!!["name"].toString()
                description.text = document.data!!["description"].toString()
                Glide.with(this).load(document.data!!["image1"]).into(image1)
                Glide.with(this).load(document.data!!["image2"]).into(image2)


                updateAllAverages(id)
            }

val fave=findViewById<ImageView>(R.id.faveourite)
        fave.setOnClickListener{
            Toast.makeText(
                this,
                "Uvjereni smo da ti je omiljena lokacija, ali ovo još nije dostupno.",
                Toast.LENGTH_LONG
            ).show()
        }

        val findOnMap=findViewById<ImageView>(R.id.findItOnMap)
        findOnMap.setOnClickListener{
            var latitude : Double=0.0
            var longitude:Double=0.0
            docRef.get()
                .addOnSuccessListener { document ->
                     latitude= document.data!!["latitude"].toString().toDouble()
                     longitude= document.data!!["longitude"].toString().toDouble()
                }
                .addOnCompleteListener {
                    CameraBounds.showSpecifiedLocationOnMap = true
                    var camerapostion: CameraPosition = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude),19f)
                    CameraBounds.camerapostion=camerapostion
                    CameraBounds.showSpecifiedLocationOnMap = true
                    CameraBounds.setCoordinates(latitude,longitude)
                    SavedStates.setnavigationBarIndex(0)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
        }
        val excitementRating = findViewById<RatingBar>(R.id.ratingBar_excitement)
        val accessibilityRating = findViewById<RatingBar>(R.id.ratingBar_accessibility)
        val originalityRating = findViewById<RatingBar>(R.id.ratingBar_originality)
        val photogenicRating = findViewById<RatingBar>(R.id.ratingBar_photogenic)
        val timeWorthRadioButtonGroup=findViewById<RadioGroup>(R.id.radioGroup)
        user?.let {
            db.collection("users")
                .document(it.uid).collection("documents")
                .document("${id}").get().addOnSuccessListener { it ->
                if (it.exists()) {
                    if (it.data?.get("excitementRating") != null) {
                        excitementRating.rating = it.data!!["excitementRating"].toString().toFloat()
                        excitementRating.setIsIndicator(true)
                    }
                    if (it.data?.get("accessibilityRating") != null) {
                        accessibilityRating.rating =
                            it.data!!["accessibilityRating"].toString().toFloat()
                        accessibilityRating.setIsIndicator(true)
                    }
                    if (it.data?.get("originalityRating") != null) {
                        originalityRating.rating =
                            it.data!!["originalityRating"].toString().toFloat()
                        originalityRating.setIsIndicator(true)
                    }
                    if (it.data?.get("photogenicRating") != null) {
                        photogenicRating.rating = it.data!!["photogenicRating"].toString().toFloat()
                        photogenicRating.setIsIndicator(true)
                    }
                    if ( it.data?.get("timeWorthButtonId")!= null) {
                        val textseleced=it.data?.get("timeWorthButtonId").toString()
                        for(button in timeWorthRadioButtonGroup){
                            val chip=findViewById<RadioButton>(button.id)
                            if(chip.text==textseleced)
                                timeWorthRadioButtonGroup.check(chip.id)
                        }
                        TimeWorthButtoIsChecked= true
                        findViewById<RadioButton>(R.id.radioButton).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton2).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton3).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton4).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton5).setClickable(false)
                    }
                }
                updateAllAverages(id)
            }
        }
        val saveRatingsButton = findViewById<Button>(R.id.saveRatings)
        saveRatingsButton?.setOnClickListener {
            user?.let {
                db.collection("users")
                    .document(it.uid).collection("documents")
                    .document("${id}").get()
                    .addOnSuccessListener { it2 ->
                    if (!it2.exists()) {
                        val data = hashMapOf(
                            "exists" to "true"
                        )
                        user.uid.let { it1 ->  db.collection("users")
                            .document(it1).collection("documents")
                            .document("${id}").set(data) }
                    }
                }
            }
            updateRating(excitementRating, id, "excitement")
            updateRating(accessibilityRating, id, "accessibility")
            updateRating(originalityRating, id, "originality")
            updateRating(photogenicRating, id, "photogenic")
            if (TimeWorthButtoIsChecked==false) {
                val selected = timeWorthRadioButtonGroup.checkedRadioButtonId
                if (selected != -1) {
                    var timeworth: Double = 0.0
                    val selectedText = findViewById<RadioButton>(selected).text
                    when (selectedText) {
                        "10sek" -> { timeworth = 10.0 }
                        "1min" -> { timeworth = 60.0 }
                        "5min" -> { timeworth = 300.0 }
                        "20min" -> { timeworth = 1200.0 }
                        "1h" -> { timeworth = 3600.0 }
                        else -> { Toast.makeText(this, "An error occured with time selection", Toast.LENGTH_SHORT).show() }
                    }
                    db.collection("places").document(id).get().addOnSuccessListener { document ->
                        var timeWorthCount = document.data!!["timeWorthCount"].toString().toDouble()
                        var timeWorthSum = document.data!!["timeWorthSum"].toString().toDouble()
                        timeWorthSum += timeworth
                        timeWorthCount += 1
                        val average: Double = timeWorthSum / timeWorthCount / 60
                        findViewById<RadioButton>(R.id.radioButton).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton2).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton3).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton4).setClickable(false)
                        findViewById<RadioButton>(R.id.radioButton5).setClickable(false)
                        user?.let {
                            db.collection("users")
                                .document(it.uid).collection("documents")
                                .document(id).update("timeWorthButtonId", selectedText)
                        }
                        db.collection("places").document(id).update(
                            "timeWorthSum", timeWorthSum,
                            "timeWorthCount", timeWorthCount,
                            "timeWorthAverage", average
                        ).addOnSuccessListener {Toast.makeText(this, "Spremljeno", Toast.LENGTH_SHORT).show()  }
                        TimeWorthButtoIsChecked=true
                        updateAllAverages(id)
                    }
                }
            }
            updateAllAverages(id)
        }
        val backButton = findViewById<ImageView>(R.id.button2)
        backButton?.setOnClickListener {
            finish()
        }
   }

    @SuppressLint("SetTextI18n")  //sluzi samo za updateanje na viewvu
    fun updateAllAverages(id: String) {
        val excitementAverage = findViewById<TextView>(R.id.excitementAverage)
        val accessibilityAverage = findViewById<TextView>(R.id.accessibilityAverage)
        val originalityAverage = findViewById<TextView>(R.id.originalityAverage)
        val photogenicAverage = findViewById<TextView>(R.id.photogenicAverage)
        val timeWorthAverage = findViewById<TextView>(R.id.timeWorthAverage)
        val docRef = db.collection("places").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                excitementAverage?.text =
                    "("+DecimalFormat("#.00").format(document.data!!["excitementAverage"]) + ") ⭐"
                accessibilityAverage?.text =
                    "("+ DecimalFormat("#.00").format(document.data!!["accessibilityAverage"]) + ") ⭐"
                originalityAverage?.text =
                    "("+DecimalFormat("#.00").format(document.data!!["originalityAverage"]) + ") ⭐"
                photogenicAverage?.text =
                    "("+DecimalFormat("#.00").format(document.data!!["photogenicAverage"]) + ") ⭐"
                val initalNumber=document.data!!["timeWorthAverage"].toString().toDouble()
                if(initalNumber<1.0)
                    timeWorthAverage?.text =(initalNumber*100).toString()+" sek"
                else{
                    var ostatak=initalNumber-(initalNumber.toInt().toDouble())
                    ostatak=ostatak*60/100
                    val finalNumber=initalNumber+ostatak
                    timeWorthAverage?.text=DecimalFormat("#.00").format(finalNumber) + " min"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Loading failed", Toast.LENGTH_LONG).show()
            }
    }

    fun updateRating(rating:RatingBar, id:String,category:String){
        if (rating.rating.toString() != "0.0" && !rating.isIndicator) {
            db.collection("places").document(id).get().addOnSuccessListener { document ->
                var Count = document.data!!["${category}"+ "Count"].toString().toDouble()
                var Sum = document.data!!["${category}"+"Sum"].toString().toDouble()
                Sum+=rating.rating.toDouble()
                Count+=1
                var average:Double=Sum/Count
                user?.let {
                    db.collection("users")
                        .document(it.uid).collection("documents")
                        .document("${id}")
                        .update("${category}"+"Rating", rating.rating.toDouble())
                }
                rating.setIsIndicator(true)
                db.collection("places").document(id).update(
                    "${category}"+"Sum", Sum,
                    "${category}"+"Count", Count,
                    "${category}"+"Average", average
                )
                    .addOnSuccessListener { Toast.makeText(this,"Spremljeno!", Toast.LENGTH_SHORT).show() }
                updateAllAverages(id)
            }
        }
    }
}