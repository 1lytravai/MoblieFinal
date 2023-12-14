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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TopicAdapter.kt
class WordAdapter(private val activity: Activity,
                  private val wordList: List<Word>,
                   ) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewWordName: TextView = itemView.findViewById(R.id.tvNameWord)
        val textViewMeaning: TextView = itemView.findViewById(R.id.tvMeaning)
        val ivStar: ImageView = itemView.findViewById(R.id.ivStar)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val currentItem = wordList[position]
        holder.textViewWordName.text = currentItem.word
        holder.textViewMeaning.text = currentItem.meaning

        holder.ivStar.setOnClickListener {
            showDeleteConfirmationDialog(activity) {
                val wordId = currentItem.wordId ?: ""
                Log.d("TopicAdapter", "Deleting topic with ID: $wordId")
                deleteTopic(wordId)
            }
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Topic")
        builder.setMessage("Are you sure you want to delete this topic?")

        builder.setPositiveButton("Yes") { _, _ ->
            onConfirm.invoke()
        }
        builder.setNegativeButton("No") { _, _ ->
            // Do nothing if the user cancels
        }

        builder.show()
    }

    private fun deleteTopic(wordId: String) {
        Log.d("TopicAdapter", "Deleting topic with ID: $wordId")

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("words").child(wordId)

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
        return wordList.size
    }
}
