package com.example.mymapapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymapapplication.Location
import com.example.mymapapplication.R

class ListLocationAdapter(context: Context): Adapter<ListLocationAdapter.ListLocationViewHolder>() {
    lateinit var onClicks : (Int) -> Unit

    var listLocation = mutableListOf<Location>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    class ListLocationViewHolder(view :View):ViewHolder(view) {
        val tvLocation: TextView by lazy { view.findViewById<TextView>(R.id.tv_location) }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListLocationAdapter.ListLocationViewHolder {
        return ListLocationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_location,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ListLocationAdapter.ListLocationViewHolder,
        position: Int
    ) {
        val item = listLocation[position]
        holder.tvLocation.setOnClickListener {
        }
        holder.tvLocation.text = "LatLng ${item.latLng} - LongLng ${item.longLng}  - Time ${item.timeSnap} - Count $position"
    }

    override fun getItemCount(): Int = listLocation.size
}