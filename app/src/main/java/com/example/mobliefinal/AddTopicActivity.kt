package com.example.mobliefinal

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

class AddTopicActivity : AppCompatActivity() {

    private lateinit var etNameTopic: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAdd: Button
    private lateinit var switchPublic: Switch

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val USER_PREFS = "userPrefs"
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
        const val PROFILE_IMAGE_KEY = "profileImage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        etNameTopic = findViewById(R.id.etNameTopic)
        etDescription = findViewById(R.id.etDescription)
        btnAdd = findViewById(R.id.btnAddNewTopic)
        switchPublic = findViewById(R.id.switchPublic)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        btnAdd.setOnClickListener {
            if (isInputValid()) {
                saveTopicToRealtimeDatabase()
            } else {
                Toast.makeText(this, "Please enter both name and description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInputValid(): Boolean {
        val nameTopic = etNameTopic.text.toString()
        val description = etDescription.text.toString()

        return nameTopic.isNotBlank() && description.isNotBlank()
    }

    private fun saveTopicToRealtimeDatabase() {
        val username = sharedPreferences.getString(USERNAME_KEY, null)

        val nameTopic = etNameTopic.text.toString()
        val description = etDescription.text.toString()

        val databaseReference = FirebaseDatabase.getInstance().reference
        val topicsRef = databaseReference.child("topics")

        val topicId = topicsRef.push().key ?: ""

        val timestamp = ServerValue.TIMESTAMP

        val isPublic = switchPublic.isChecked // Lấy giá trị từ Switch

        val topic = hashMapOf(
            "topicId" to topicId,
            "name" to nameTopic,
            "description" to description,
            "user" to username,
            "createdAt" to timestamp,
            "public" to isPublic,
            "favorite" to false,
            "folder" to ""
        )

        topicsRef.child(topicId).setValue(topic)
            .addOnSuccessListener {
                Toast.makeText(this, "Topic added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add topic: $e", Toast.LENGTH_SHORT).show()
            }
    }

}
