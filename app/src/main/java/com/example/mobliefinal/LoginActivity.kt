package com.example.mobliefinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignup: TextView
    private lateinit var tvForget: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val USER_PREFS = "userPrefs"
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
        const val EMAIL_KEY = "email"
        const val PROFILE_IMAGE_KEY = "profileImage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPass)
        btnLogin = findViewById(R.id.btnLogin)
        tvForget = findViewById(R.id.tvForgetPass)
        tvSignup = findViewById(R.id.tvRegister)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        // Check if user is already authenticated
        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        tvForget.setOnClickListener {
            val intent = Intent(this@LoginActivity, ConfirmCode::class.java)
            startActivity(intent)
        }

        tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener { loginUser() }

    }

    private fun loginUser() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)

                    if (user != null) {
                        if (user.password == password) {
                            saveUserInfo(user)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            // Incorrect password
                            Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserInfo(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(USERNAME_KEY, user.username)
        editor.putString(EMAIL_KEY, user.email)
        editor.putString(PROFILE_IMAGE_KEY, user.profileImage)
        editor.apply()
    }
}
