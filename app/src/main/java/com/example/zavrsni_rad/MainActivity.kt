package com.example.zavrsni_rad

import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.zavrsni_rad.ui.map.MapFragment
import com.example.zavrsni_rad.ui.popup.PopUpFragment
import com.example.zavrsni_rad.ui.preferences.PreferencesFragment
import com.example.zavrsni_rad.ui.preferences.SavedUserChips
import com.example.zavrsni_rad.ui.profile.ProfileFragment
import com.example.zavrsni_rad.ui.rank.LocationRankingFragment
import com.example.zavrsni_rad.ui.rank.SavedStates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore
    private var allowRefresh = false
    var navigationBarIndex:Int=SavedStates.getnavigationBarIndex()

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bottom_bar= findViewById<AnimatedBottomBar>(R.id.bottom_bar)
        supportActionBar?.hide()
        bottom_bar.selectTabAt(SavedStates.navigationBarIndex)
        loadFragmentByIndex(navigationBarIndex)
        val newUser: String? =intent.getStringExtra("registration")


        user?.let {
            db.collection("users")
                .document(user.uid).collection("documents")
                .document("categoryPreferences").get()
                .addOnSuccessListener { it ->
                    val selectedChipsArray=it.data!!["selectedChips"]as ArrayList<String>
                    SavedUserChips.addItems(selectedChipsArray)
                }
        }


if(newUser=="true") {
    val showPopUp = PopUpFragment()
    showPopUp.show(supportFragmentManager, "showPopUp")
    //navigationBarIndex = 1
    loadFragmentByIndex(navigationBarIndex)
    bottom_bar.selectTabAt(SavedStates.navigationBarIndex)

}
        bottom_bar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                //navigationBarIndex=newIndex
                SavedStates.setnavigationBarIndex(newIndex)
                loadFragmentByIndex(newIndex)

            }


        })
    }


    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
        onResumeFragments()
        if(allowRefresh){
            allowRefresh=false
             loadFragmentByIndex(SavedStates.navigationBarIndex)

            }
    }

    override fun onPause() {
        super.onPause()
        if (!allowRefresh) allowRefresh = true

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    fun loadFragmentByIndex(index:Int){
        SavedStates.setnavigationBarIndex(index)
        when(index){
            0-> {
                loadFragment(MapFragment())

                true
            }
            1->{
                loadFragment(PreferencesFragment())
                true
            }
            2 -> {
                loadFragment(LocationRankingFragment())
                true
            }
            3 -> {
                loadFragment(ProfileFragment())
                true
            }

            else -> {
                loadFragment(ProfileFragment())
                true
            }
       }
    }
}