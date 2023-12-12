package com.example.mobliefinal

import android.app.Activity
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
class TopicAdapter(private val activity: Activity,
                   private val topicList: List<Topic>,
                   ) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTopicName: TextView = itemView.findViewById(R.id.tvNameTopic)
        val textViewTopicDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val textViewUser: TextView = itemView.findViewById(R.id.tvUsername)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        val cardView: LinearLayout = itemView.findViewById(R.id.cardView)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val currentTopic = topicList[position]
        holder.textViewTopicName.text = currentTopic.name
        holder.textViewTopicDescription.text = currentTopic.description
        holder.textViewUser.text = currentTopic.user

        holder.ivDelete.setOnClickListener {
            deleteItem(position)
        }

        holder.cardView.setOnClickListener{
            val intent = Intent(activity, AddWordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private fun deleteItem(position: Int) {
        val currentTopic = topicList[position]

        val topicId = currentTopic.topicId.toString()
        Log.d("FolderAdapter", "Đang xóa chủ đề với ID: $topicId")

        val databaseReference = FirebaseDatabase.getInstance().getReference("topics").child(topicId)
        databaseReference.removeValue()
            .addOnSuccessListener {
                Log.d("FolderAdapter", "Data deleted successfully")
                notifyItemRemoved(position)
            }
            .addOnFailureListener {
                Log.e("FolderAdapter", "Error deleting data", it)
            }
    }

    override fun getItemCount(): Int {
        return topicList.size
    }
}
