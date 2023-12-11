package com.example.mobliefinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FolderAdapter(private val folderList: List<Folder>) :
    RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFolderName: TextView = itemView.findViewById(R.id.tvNameFolder)
        val textViewFolderDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val textViewUser: TextView = itemView.findViewById(R.id.tvUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val currentItem = folderList[position]
        holder.textViewFolderName.text = currentItem.name
        holder.textViewFolderDescription.text = currentItem.description
        holder.textViewUser.text = currentItem.user
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
}
