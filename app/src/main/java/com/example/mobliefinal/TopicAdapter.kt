package com.example.mobliefinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

// TopicAdapter.kt
class TopicAdapter(
    private val topicList: List<Topic>,
//    private val databaseReference: DatabaseReference
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    interface OnStarClickListener {
        fun onStarClick(position: Int)
    }


    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTopicName: TextView = itemView.findViewById(R.id.tvNameTopic)
        val textViewTopicDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val textViewUser: TextView = itemView.findViewById(R.id.tvUsername)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
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
        
        
        holder.ivDelete.setOnClickListener{
//            deleteTopic(position)
        }
    }



    override fun getItemCount(): Int {
        return topicList.size
    }
}
