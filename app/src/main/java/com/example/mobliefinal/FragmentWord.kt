package com.example.mobliefinal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobliefinal.databinding.ActivityMainTopicBinding
import com.example.mobliefinal.databinding.FragmentTopicBinding
import com.example.mobliefinal.databinding.FragmentWordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentWord : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var wordList: ArrayList<Word>
    private lateinit var wordAdapter: WordAdapter
    private var _binding: FragmentWordBinding? = null
    private lateinit var ivAdd: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topicId = arguments?.getString("topicId")
        val topicName = arguments?.getString("topicName")

        ivAdd = binding.ivAdd
        ivAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddWordActivity::class.java)
            intent.putExtra("topicId", topicId)
            intent.putExtra("topicName", topicName)
            startActivity(intent)
        }

        recyclerView = binding.recyclerViewWord
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference.child("words")

        wordList = ArrayList()
        wordAdapter = WordAdapter(requireActivity(), wordList)
        recyclerView.adapter = wordAdapter

        if (!topicId.isNullOrEmpty()) {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    wordList.clear()
                    for (postSnapshot in snapshot.children) {
                        val word = postSnapshot.getValue(Word::class.java)
                        word?.let {
                            if (it.topic == topicId) {
                                wordList.add(it)
                            }
                        }
                    }
                    wordAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                }
            })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}