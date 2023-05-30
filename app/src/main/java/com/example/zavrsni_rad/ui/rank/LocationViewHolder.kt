package com.example.zavrsni_rad.ui.rank
import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zavrsni_rad.LocationData
import com.example.zavrsni_rad.R
import java.text.DecimalFormat

class LocationViewHolder (val view: View,val spinnerSelectedIndex:Int): RecyclerView.ViewHolder(view)
{
    private val locationImage =
        view.findViewById<ImageView>(R.id.location_image)
    private val locationName =
        view.findViewById<TextView>(R.id.location_name)
    val averageGradeTextView =
        view.findViewById<TextView>(R.id.averageGrade)
    val numberRank =
        view.findViewById<TextView>(R.id.rankingplace)
    val medal =
        view.findViewById<ImageView>(R.id.medalplace)
    @SuppressLint("SetTextI18n")
    fun bind(
        index: Int,
        location: LocationData
    )
    {
        Glide.with(view.context).load(location.image2).into(locationImage)
        locationName.text = location.name
        medal.visibility=View.VISIBLE
        if(index==0){medal.setImageResource(R.drawable.medal_1st)}
        if(index==1){medal.setImageResource(R.drawable.medal_2nd)}
        if(index==2){medal.setImageResource(R.drawable.medal_3rd)}
        if(index>=3){
            medal.visibility=View.GONE
            numberRank.visibility= View.VISIBLE
            numberRank.text= (index+1).toString()+"."
        }
        else {
            numberRank.visibility = View.GONE

        }
        when(spinnerSelectedIndex){
            0->{if(location.excitementAverage.toString()=="0.0")
                averageGradeTextView?.text ="(0.00) ⭐"
            else{ averageGradeTextView?.text =
                "("+ DecimalFormat("#.00").format(location.excitementAverage)+")⭐" }
            }
            1->{if(location.accessibilityAverage.toString()=="0.0")
                averageGradeTextView?.text ="(0.00) ⭐"
            else
                averageGradeTextView?.text = "("+  DecimalFormat("#.00").format(location.accessibilityAverage)+")⭐"
            }
            2->{if(location.originalityAverage.toString()=="0.0")
                averageGradeTextView?.text ="(0.00) ⭐"
            else
                averageGradeTextView?.text =
                    "("+  DecimalFormat("#.00").format(location.originalityAverage)+")⭐"
            }
            3->{
                if(location.photogenicAverage.toString()=="0.0")
                    averageGradeTextView?.text ="(0.00) ⭐"
                else
                    averageGradeTextView?.text =
                        "("+  DecimalFormat("#.00").format(location.photogenicAverage)+")⭐"
            }
            4->{
                val initalNumber=location.timeWorthAverage!!.toDouble()
                show(initalNumber)
            }
        }
    }

    fun show(initalNumber:Double) {
        if(initalNumber==0.0){
            averageGradeTextView?.text ="0.0 sek"
        }
        else if(initalNumber<1.0)
            averageGradeTextView?.text =DecimalFormat("#.00").format(initalNumber*100).toString()+" sek"

        else{
            var remainder=initalNumber-(initalNumber.toInt().toDouble())
            remainder=remainder*60/100
            val finalFormatAsMintues=initalNumber+remainder
            averageGradeTextView?.text=DecimalFormat("#.00").format(finalFormatAsMintues) + " min"
        }
    }
}