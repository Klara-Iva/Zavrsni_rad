package com.example.zavrsni_rad.ui.rank

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zavrsni_rad.LocationDetailsActivity
import com.example.zavrsni_rad.LocationData
import com.example.zavrsni_rad.R


class LocationRecyclerAdapter(val items: ArrayList<LocationData>, val spinner:Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {

        return LocationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_component,
                parent, false
            ),spinner
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                holder.bind(position, items[position])
            }
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, LocationDetailsActivity::class.java)
            intent.putExtra("id",items[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}