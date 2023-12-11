package com.example.mobliefinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Properties
import java.util.Random
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ConfirmCode: AppCompatActivity() {
    private lateinit var btnConfirm: Button
    private lateinit var btnReceiveMail: Button
    private lateinit var etConfirmCode: EditText
    private lateinit var etUsername: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_code)

        btnConfirm = findViewById(R.id.btnConfirmCode)
        btnReceiveMail = findViewById(R.id.btnReceiveMail)
        etConfirmCode = findViewById(R.id.etConfirmCode)
        etUsername = findViewById(R.id.etUsername)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        auth = FirebaseAuth.getInstance()

        // vô hiệu hóa etConfirmCode và btnConfirm
        etConfirmCode.isEnabled = false
        btnConfirm.isEnabled = false

        btnReceiveMail.setOnClickListener {
            val username = etUsername.text.toString()

            databaseReference.child(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val email =
                                snapshot.child("email").value.toString()
                            Toast.makeText(
                                this@ConfirmCode,
                                "Confirmation code gave in your email. Please enter code to change password",
                                Toast.LENGTH_SHORT
                            ).show()

                            etConfirmCode.isEnabled = true
                            btnConfirm.isEnabled = true

                            val editor = sharedPreferences.edit()
                            editor.putString("username", username)
                            editor.putString("email", email)
                            editor.apply()

                            btnReceiveMail.isEnabled = false
                            etUsername.isEnabled = false

                            // Gửi mã
                            buttonSendEmail(email)
                        } else {
                            Toast.makeText(
                                this@ConfirmCode,
                                "Username not found",
                                Toast.LENGTH_SHORT
                            ).show()

                            etConfirmCode.isEnabled = false
                            btnConfirm.isEnabled = false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@ConfirmCode,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

        btnConfirm.setOnClickListener {
            val enteredCode = etConfirmCode.text.toString()
            val savedCode = sharedPreferences.getString("confirmationCode", "")

            if (enteredCode == savedCode) {
                val username = etUsername.text.toString()
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()

                val intent = Intent(this@ConfirmCode, ForgotPasswordActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@ConfirmCode, "Incorrect confirmation code", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun buttonSendEmail(email: String) {
        try {
            val stringSenderEmail = "vate202@gmail.com"
            val stringPasswordSenderEmail = "lktyqjjjbiyefldc"
            val stringHost = "smtp.gmail.com"

            val properties = Properties()
            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                }
            })

            val mimeMessage = MimeMessage(session)
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(email))

            val confirmationCode = generateConfirmationCode()
            mimeMessage.subject = "Password Reset Confirmation Code"
            mimeMessage.setText("Your confirmation code is: $confirmationCode")

            val editor = sharedPreferences.edit()
            editor.putString("confirmationCode", confirmationCode)
            editor.apply()

            val thread = Thread {
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
            thread.start()

        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun generateConfirmationCode(): String {
        val random = Random()
        val confirmationCode = (100000 + random.nextInt(900000)).toString()
        return confirmationCode
    }

}