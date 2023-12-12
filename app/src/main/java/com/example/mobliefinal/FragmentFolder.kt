package com.example.mobliefinal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobliefinal.databinding.FragmentFolderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentFolder : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var folderList: ArrayList<Folder>
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewFolder // Sử dụng binding để truy cập các thành phần trong layout
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        databaseReference = FirebaseDatabase.getInstance().reference.child("folders")

        folderList = ArrayList()
        folderAdapter = FolderAdapter(folderList)
        recyclerView.adapter = folderAdapter

        sharedPreferences = requireActivity().getSharedPreferences(FragmentProfile.USER_PREFS, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(FragmentProfile.USERNAME_KEY, "")
        Log.d("Username", username ?: "Username is empty or null")

        if (!username.isNullOrEmpty()) {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    folderList.clear()
                    for (postSnapshot in snapshot.children) {
                        val folder = postSnapshot.getValue(Folder::class.java)
                        folder?.let {
                            if (it.user == username) {
                                folderList.add(it)
                            }
                        }
                    }
                    folderAdapter.notifyDataSetChanged()
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