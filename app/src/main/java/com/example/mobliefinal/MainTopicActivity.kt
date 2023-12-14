package com.example.mobliefinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.mobliefinal.databinding.ActivityMainBinding
import com.example.mobliefinal.databinding.ActivityMainTopicBinding

class MainTopicActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainTopicBinding
    lateinit var tvNameTopic: TextView
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvNameTopic = findViewById(R.id.tvNameTopic)
        val topicId = intent.getStringExtra("topicId")
        val topicName = intent.getStringExtra("topicName")
        tvNameTopic.text = topicName

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        replaceFragment(FragmentWord())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_main -> replaceFragment(FragmentWord())
                //fragment learn
                else -> {

                }
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle().apply {
            val topicId = intent.getStringExtra("topicId")
            val topicName = intent.getStringExtra("topicName")
            putString("topicId", topicId)
            putString("topicName", topicName)
        }

        fragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}