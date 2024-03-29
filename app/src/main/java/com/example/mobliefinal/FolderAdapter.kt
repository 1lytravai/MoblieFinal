package com.example.mobliefinal

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class FolderAdapter(private val activity: Activity,
                    private val folderList: List<Folder>) :
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
            showDeleteConfirmationDialog(activity) {
                val folderId = currentItem.folderId ?: ""
                Log.d("TopicAdapter", "Deleting topic with ID: $folderId")
                deleteTopic(folderId)
            }

        }
    }

    private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Folder")
        builder.setMessage("Are you sure you want to delete this folder?")

        builder.setPositiveButton("Yes") { _, _ ->
            onConfirm.invoke()
        }
        builder.setNegativeButton("No") { _, _ ->
            // Do nothing if the user cancels
        }

        builder.show()
    }

    private fun deleteTopic(folderId: String) {
        Log.d("FolderAdapter", "Deleting folder with ID: $folderId")

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("folders").child(folderId)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Dữ liệu tồn tại, có thể xóa
                    databaseReference.removeValue()
                    Log.d("TopicAdapter", "Data deleted successfully")
                } else {
                    // Dữ liệu không tồn tại
                    Log.e("TopicAdapter", "Data does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TopicAdapter", "Error deleting data", error.toException())
            }
        }

        // Lắng nghe sự kiện xóa
        databaseReference.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
}
