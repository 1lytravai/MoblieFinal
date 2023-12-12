package com.example.mobliefinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class FolderAdapter(private val folderList: List<Folder>) :
    RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFolderName: TextView = itemView.findViewById(R.id.tvNameFolder)
        val textViewFolderDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val textViewUser: TextView = itemView.findViewById(R.id.tvUsername)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
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
        holder.ivDelete.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("folders")
            databaseReference.child(currentItem.name.toString()).removeValue()
                 .addOnSuccessListener {
                        Log.d("FolderAdapter", "Folder deleted successfully")
                    }
                    .addOnFailureListener {
                        Log.e("FolderAdapter", "Error deleting folder", it)
                    }

        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
}
