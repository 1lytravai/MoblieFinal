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

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var btnChange: Button
    private lateinit var etNewPass: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var tvLogin: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnChange = findViewById(R.id.btnChangePass)
        etConfirmPass = findViewById(R.id.etConfirmNewPass)
        etNewPass = findViewById(R.id.etNewPass)
        tvLogin = findViewById(R.id.tvLogin)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        tvLogin.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnChange.setOnClickListener {
            // username từ SharedPreferences
            val username = sharedPreferences.getString("username", "")

            val newPass = etNewPass.text.toString()
            val confirmPass = etConfirmPass.text.toString()

            var isValid = true
            var errorMessage = ""

            if (newPass.length < 6) {
                isValid = false
                errorMessage = "New password must be at least 6 characters"
            } else if (confirmPass.length < 6) {
                isValid = false
                errorMessage = "Please confirm password with at least 6 characters"
            } else if (newPass != confirmPass) {
                isValid = false
                errorMessage = "New password and confirm password do not match"
            }

            if (isValid) {
                // Cập nhật thông tin mới vào Realtime Database
                val userUpdates = HashMap<String, Any>()
                userUpdates["password"] = newPass

                databaseReference.child("users/$username").updateChildren(userUpdates)
                    .addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Password updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Failed to update password in the database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this@ForgotPasswordActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
