package com.example.mobliefinal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobliefinal.databinding.FragmentTopicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTopic : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var topicList: ArrayList<Topic>
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentTopicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewTopic
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference.child("topics")

        topicList = ArrayList()
        topicAdapter = TopicAdapter(topicList)
        recyclerView.adapter = topicAdapter

        sharedPreferences = requireActivity().getSharedPreferences(FragmentProfile.USER_PREFS, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(FragmentProfile.USERNAME_KEY, "")
        Log.d("Username", username ?: "Username is empty or null")

            if (!username.isNullOrEmpty()) {
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        topicList.clear()
                        for (postSnapshot in snapshot.children) {
                            val topic = postSnapshot.getValue(Topic::class.java)
                            topic?.let {
                                if (it.user == username) {
                                    topicList.add(it)
                                }
                            }
                        }
                        topicAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
            }
        }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
