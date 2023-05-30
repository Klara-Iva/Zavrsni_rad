package com.example.zavrsni_rad.ui.preferences

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.example.zavrsni_rad.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PreferencesFragment: Fragment() {

    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore
    var selectedChipsTextArray: ArrayList<String> = arrayListOf()
    var chipTextArrayForSetup: ArrayList<String> = arrayListOf()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_preferences,container, false )

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupCategories)
        chipTextArrayForSetup.addAll(SavedUserChips.getChips())
        setCheckedChips(view)
        chipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chipsId = group.checkedChipIds
            selectedChipsTextArray.clear()
            for (id in chipsId) {
                val chip = group.findViewById<Chip>(id!!)
                selectedChipsTextArray.add(chip.text.toString())
            }
        }
        val savePreferencesButton=view.findViewById<Button>(R.id.savePreferencesButton)
        savePreferencesButton.setOnClickListener{
            SavedUserChips.addItems(selectedChipsTextArray)
            user?.let {
                db.collection("users")
                    .document(it.uid).collection("documents")
                    .document("categoryPreferences")
                    .update("selectedChips", selectedChipsTextArray)
                    .addOnSuccessListener { Toast.makeText(context,"Spremljeno!",Toast.LENGTH_SHORT).show() }
            }
        }
        return view
    }

    fun setCheckedChips(view:View){
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupCategories)
        if(chipTextArrayForSetup.size!=0){
            for(chipText in chipTextArrayForSetup){
                if (chipGroup != null) {
                    for(chip in chipGroup){
                        val currentChip=view.findViewById<Chip>(chip.id)
                        if(currentChip?.text==chipText)
                            currentChip.isChecked=true
                    }
                }
            }
        }
    }
}


