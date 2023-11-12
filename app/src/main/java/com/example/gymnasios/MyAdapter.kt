package com.example.gymnasios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(
    private val streetWorkoutList: ArrayList<StreetWorkout>,
    private val onItemClick: (StreetWorkout) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val streetImage: ImageView = itemView.findViewById(R.id.title_image)
        val streetName: TextView = itemView.findViewById(R.id.gymheading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = streetWorkoutList[position]
        holder.streetImage.setImageResource(currentItem.titleImage)
        holder.streetName.text = currentItem.street
        holder.itemView.setOnClickListener { onItemClick(currentItem) }
    }

    override fun getItemCount() = streetWorkoutList.size
}