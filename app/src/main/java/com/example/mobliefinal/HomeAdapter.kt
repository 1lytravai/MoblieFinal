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

class HomeAdapter(private val activity: Activity,
                  private val topicList: List<Topic>,
                   ) : RecyclerView.Adapter<HomeAdapter.TopicViewHolder>() {

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTopicName: TextView = itemView.findViewById(R.id.tvNameTopic)
        val textViewTopicDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val textViewUser: TextView = itemView.findViewById(R.id.tvUsername)
        val cardView: LinearLayout = itemView.findViewById(R.id.cardView)
        val ivStar: ImageView = itemView.findViewById(R.id.ivStar)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return TopicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val currentTopic = topicList[position]
        holder.textViewTopicName.text = currentTopic.name
        holder.textViewTopicDescription.text = currentTopic.description
        holder.textViewUser.text = currentTopic.user

        holder.cardView.setOnClickListener {
            val intent = Intent(activity, MainTopicActivity::class.java)

            intent.putExtra("topicId", currentTopic.topicId)
            intent.putExtra("topicName", currentTopic.name)
            activity.startActivity(intent)
        }
        }

    override fun getItemCount(): Int {
        return topicList.size
    }
}
