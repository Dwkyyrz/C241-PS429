package com.TeamBangkit.animaldetection.View.History

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.TeamBangkit.animaldetection.Data.DB.ClassificationHistory
import com.TeamBangkit.animaldetection.R
import com.TeamBangkit.animaldetection.View.History.detail_item.HistoryDetail

class ListHistoryAdapter(
    private var listHistory: ArrayList<ClassificationHistory>,
    private val onDeleteClick: (ClassificationHistory) -> Unit
    ) : RecyclerView.Adapter<ListHistoryAdapter.ListViewholder>() {

    class ListViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewholder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_animal, parent, false)
        return ListViewholder(view)
    }

    override fun onBindViewHolder(holder: ListViewholder, position: Int) {
        val history = listHistory[position]
        Glide.with(holder.itemView.context).load(history.imageUri).into(holder.imgPhoto)
        holder.tvName.text = history.name
        holder.tvDescription.text = history.description

        //intent data ke halaman detail
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HistoryDetail::class.java).apply {
                putExtra("imageUri", history.imageUri)
                putExtra("name", history.name)
                putExtra("description", history.description)
                putExtra("age", history.age)
                putExtra("probability", history.probability.toString())
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, history)
        }
    }

    override fun getItemCount(): Int = listHistory.size

    private fun showDeleteConfirmationDialog(context: Context, history: ClassificationHistory) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Are you sure you want to delete this history?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                onDeleteClick(history)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Delete Confirmation")
        alert.show()
    }

    fun removeItem(history: ClassificationHistory) {
        val position = listHistory.indexOf(history)
        if (position != -1) {
            listHistory.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newList: ArrayList<ClassificationHistory>) {
        listHistory = newList
        notifyDataSetChanged()
    }
}