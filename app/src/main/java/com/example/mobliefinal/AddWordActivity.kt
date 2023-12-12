package com.example.mobliefinal

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar

class AddWordActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var etWord: EditText
    private lateinit var etMeaning: EditText
    private lateinit var btnAdd: Button
    private lateinit var ivAddNewView: ImageView
    private lateinit var wordContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addword_layout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        wordContainer = findViewById(R.id.containerLinearLayout)

        btnAdd = findViewById(R.id.btnAdd)
        ivAddNewView = findViewById(R.id.ivAddNewView)
        ivAddNewView.setOnClickListener {
            addNewWordView()
        }

        btnAdd.setOnClickListener{
            addAllWords()
        }

    }
    fun addNewWordView() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newWordView = inflater.inflate(R.layout.activity_add_word, null)
        wordContainer.addView(newWordView)
    }

    fun addAllWords() {
        for (i in 0 until wordContainer.childCount) {
            val wordView = wordContainer.getChildAt(i)
            val etWord = wordView.findViewById<EditText>(R.id.etWord)
            val etMeaning = wordView.findViewById<EditText>(R.id.etMeaning)

            // Lấy giá trị từ EditText và thêm vào Firebase
            val word = etWord.text.toString()
            val meaning = etMeaning.text.toString()

            // Thực hiện xử lý thêm từ vào Firebase ở đây
        }
    }
}