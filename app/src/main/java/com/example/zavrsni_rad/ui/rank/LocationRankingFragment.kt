package com.example.zavrsni_rad.ui.rank


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zavrsni_rad.LocationData
import com.example.zavrsni_rad.R
import com.example.zavrsni_rad.ui.preferences.SavedUserChips
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.sin


class LocationRankingFragment:Fragment() {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: LocationRecyclerAdapter
    var changingLocationList: ArrayList<LocationData> = ArrayList()
    var initialLocationList: ArrayList<LocationData> = ArrayList()
    var spinnerIndex: Int = 0
    var selectedChips: ArrayList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location_ranking, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycledviewer)
        db.collection("places")
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val singleLocationData = data.toObject(LocationData::class.java)
                    if (singleLocationData != null) {
                        singleLocationData.id = data.id
                        singleLocationData.overallGrade= (singleLocationData.accessibilityAverage!! +singleLocationData.excitementAverage!!+singleLocationData.originalityAverage!!+singleLocationData.photogenicAverage!!)/4
                        initialLocationList.add(singleLocationData)

                    }
                }
                changingLocationList = initialLocationList
            }
            .addOnCompleteListener{updateArrayWithChips(view)}
        selectedChips.addAll(SavedUserChips.getChips())
        val categories = resources.getStringArray(R.array.categories)

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val adapter =ArrayAdapter(requireContext(), R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinnerIndex= SavedStates.getSpinnerIndex()
        spinner.setSelection(spinnerIndex)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerIndex = position
                SavedStates.setSpinnerIndex(position)
                performSort()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                recyclerAdapter = LocationRecyclerAdapter(initialLocationList,spinnerIndex)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    this.adapter = recyclerAdapter
                }
            }
        }
        return view
    }


    fun updateArrayWithChips(view:View) {
        val lista: ArrayList<LocationData> = ArrayList()
        for (chip in selectedChips) {
            for (data in initialLocationList) {
                for(data2 in data.category){
                    if (data2==chip) {
                        if (!lista.contains(data))
                            lista.add(data)
                    }
                }
            }
        }

        val nothingSelectedTextView = view.findViewById<TextView>(R.id.onNothingSelected)
        val addIcon = view.findViewById<ImageView>(R.id.add_icon)
        if (lista.isEmpty()) {
            nothingSelectedTextView?.visibility = View.VISIBLE
            addIcon?.visibility = View.VISIBLE
        } else {
            changingLocationList = lista
            performSort()
            nothingSelectedTextView?.visibility = View.GONE
            addIcon?.visibility = View.GONE
        }
    }

    fun performSort(){

        val position=spinnerIndex
        var sortedList: ArrayList<LocationData> = ArrayList()
        when(position){
            0->{
                sortedList =
                    ArrayList(changingLocationList.sortedWith(compareByDescending {
                        it.overallGrade
                    }))

            }
            1 -> {
                sortedList=
                    ArrayList(changingLocationList.sortedWith(compareByDescending{
                        it.excitementAverage
                    }))
            }
            2 -> {
                sortedList=
                    ArrayList(changingLocationList.sortedWith(compareByDescending{
                        it.accessibilityAverage
                    }))
            }
            3 -> {
                sortedList=
                    ArrayList(changingLocationList.sortedWith(compareByDescending{
                        it.originalityAverage
                    }))
            }

            4 -> {
                sortedList =
                    ArrayList(changingLocationList.sortedWith(compareByDescending {
                        it.photogenicAverage
                    }))
            }
            5 -> {
                sortedList =
                    ArrayList(changingLocationList.sortedWith(compareBy {
                        it.timeWorthAverage
                    }))
            }

        }
        if(SavedUserChips.list.isNotEmpty()){
                showList(sortedList)
        }
    }

    fun showList(list:ArrayList<LocationData>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycledviewer)
        recyclerAdapter = LocationRecyclerAdapter(list,spinnerIndex)
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = recyclerAdapter
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        val bundle=Bundle()
        bundle.putInt("spinnerindex",spinnerIndex)

    }
}