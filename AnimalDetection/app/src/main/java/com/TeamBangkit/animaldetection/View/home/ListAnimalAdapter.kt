package com.TeamBangkit.animaldetection.View.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.TeamBangkit.animaldetection.Animal
import com.TeamBangkit.animaldetection.R
import com.TeamBangkit.animaldetection.View.detail.DetailAnimal

class ListAnimalAdapter(private val listAnimal: ArrayList<Animal>) : RecyclerView.Adapter<ListAnimalAdapter.ListViewholder>() {

    class ListViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.image_poster)
        val tvName: TextView = itemView.findViewById(R.id.tv_nama_hewan)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_tahun)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewholder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_animal,parent,false)
        return ListViewholder(view)
    }

    override fun onBindViewHolder(holder: ListViewholder, position: Int) {
        val (name, years, tipe, zoo, description,photo) = listAnimal[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvDescription.text = years

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context,listAnimal[holder.adapterPosition].name, Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, DetailAnimal::class.java)
            intent.putExtra("animal_name", name)
            intent.putExtra("animal_years", years)
            intent.putExtra("animal_tipe", tipe)
            intent.putExtra("animal_zoo", zoo)
            intent.putExtra("animal_description", description)
            intent.putExtra("animal_photo", photo)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listAnimal.size
}