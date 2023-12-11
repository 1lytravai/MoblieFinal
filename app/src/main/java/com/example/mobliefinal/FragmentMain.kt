package com.example.mobliefinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment

class FragmentMain: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View =  inflater.inflate(R.layout.fragment_main, container, false)

//        val toolbar: Toolbar = view.findViewById(R.id.main_toolbar)
//        val searchView: SearchView = toolbar.findViewById(R.id.search_view)
//
//        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
//
//        searchView.queryHint = "Search heare..."
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//                    // Perform the search
//                }
//                return true
//            }

//            override fun onQueryTextChange(newText: String?): Boolean {
//
//                return true
//            }
//
//        })

//        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.setDisplayShowHomeEnabled(true)

        return view
    }

}