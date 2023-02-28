package com.example.zavrsni_rad.ui.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.example.zavrsni_rad.R

class PopUpFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_popup,container, false )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text=view.findViewById<TextView>(R.id.textView2)
        text.text="Ovo je Virovitički turistički vodič.\n" +
                " Zamišljen je kao maleni pomoćnik na kojem možete vidjeti zanimljive lokacije na karti," +
                " ali i odabrati što Vas zanima te dobiti preporuke " +
                "koje rangirate po jednom od kriterija kako bi brže pronašli što je vrijedno " +
                "posjetiti!\n\nKako biste pomogli i ostalim korisicima " +
                "u odluci što posjetiti, ne zaboravite ocjeniti svaku lokaciju!\nSretno lutanje!"

        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog);
        val btn=view.findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            dismiss()
        }
    }
}