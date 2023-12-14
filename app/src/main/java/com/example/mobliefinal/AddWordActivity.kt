package com.example.mobliefinal

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

class AddWordActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var etWord: EditText
    private lateinit var etMeaning: EditText
    private lateinit var btnAdd: Button
    private lateinit var ivAddNewView: ImageButton
    private lateinit var tvNameTopic: TextView
    private lateinit var wordContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addword_layout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val topicId = intent.getStringExtra("topicId")
        val topicName = intent.getStringExtra("topicName")
        tvNameTopic = findViewById(R.id.tvNameTopic)
        tvNameTopic.text = topicName

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        etWord = findViewById(R.id.etWord)
        etMeaning = findViewById(R.id.etMeaning)
        wordContainer = findViewById(R.id.containerLinearLayout)

        btnAdd = findViewById(R.id.btnAdd)

        ivAddNewView = findViewById(R.id.ivAddNewView)
        ivAddNewView.setOnClickListener {
            addNewWordView()
        }

        btnAdd.setOnClickListener {
            if (isInputValid()) {
                addAllWords()
            } else {
                Toast.makeText(this, "Please enter valid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInputValid(): Boolean {
        val nameTopic = etWord.text.toString()
        val description = etMeaning.text.toString()

        return nameTopic.isNotBlank() && description.isNotBlank()
    }

    private fun addNewWordView() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newWordView = inflater.inflate(R.layout.activity_add_word, null)
        wordContainer.addView(newWordView)
        etWord = newWordView.findViewById(R.id.etWord)
        etMeaning = newWordView.findViewById(R.id.etMeaning)
    }

    private fun addAllWords() {
        for (i in 0 until wordContainer.childCount) {
            val wordView = wordContainer.getChildAt(i)
            val etWord = wordView.findViewById<EditText>(R.id.etWord)
            val etMeaning = wordView.findViewById<EditText>(R.id.etMeaning)

            if (etWord != null && etMeaning != null) {
                val word = etWord.text.toString()
                val meaning = etMeaning.text.toString()

                val databaseReference = FirebaseDatabase.getInstance().reference

                // Đọc dữ liệu từ Intent
                val topicId = intent.getStringExtra("topicId")
                val topicName = intent.getStringExtra("topicName")

                // Chắc chắn rằng topicId không null
                if (topicId != null) {
                    val wordsRef = databaseReference.child("words")

                    val timestamp = ServerValue.TIMESTAMP
                    val wordId = wordsRef.push().key ?: ""

                    val words = hashMapOf(
                        "wordId" to wordId,
                        "word" to word,
                        "meaning" to meaning,
                        "topic" to topicId,
                        "createdAt" to timestamp,
                        "favorite" to false
                    )

                    wordsRef.child(wordId).setValue(words)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Words added successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to add words: $e", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Xử lý khi topicId là null (có thể xuất thông báo lỗi hoặc thực hiện hành động khác)
                    Toast.makeText(this, "Error: TopicId is null", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
