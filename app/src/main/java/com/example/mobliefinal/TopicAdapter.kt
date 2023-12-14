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

        holder.cardView.setOnClickListener {
            val intent = Intent(activity, MainTopicActivity::class.java)

            intent.putExtra("topicId", currentTopic.topicId)
            intent.putExtra("topicName", currentTopic.name)
            activity.startActivity(intent)
        }

        holder.ivDelete.setOnClickListener {
            showDeleteConfirmationDialog(activity) {
                val topicId = currentTopic.topicId ?: ""
                Log.d("TopicAdapter", "Deleting topic with ID: $topicId")
                deleteTopic(topicId)
            }

        }}
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

    private fun deleteTopic(topicId: String) {
        Log.d("TopicAdapter", "Deleting topic with ID: $topicId")

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("topics").child(topicId)

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
//        databaseReference.add(valueEventListener)
    }



    override fun getItemCount(): Int {
        return topicList.size
    }
}
