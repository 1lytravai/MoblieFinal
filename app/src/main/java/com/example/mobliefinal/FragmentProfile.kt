package com.example.mobliefinal

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.mobliefinal.LoginActivity
import com.example.mobliefinal.User
import com.example.mobliefinal.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class FragmentProfile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val USER_PREFS = "userPrefs"
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
        const val PROFILE_IMAGE_KEY = "profileImage"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        sharedPreferences = requireActivity().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        val tvEmail = binding.tvEmail
        val ivAvatar = binding.ivAvatar
        val tvPass = binding.tvPass
        val tvUsername = binding.tvUsername
        val btnLogOut = binding.btnLogOut

        val username = sharedPreferences.getString(USERNAME_KEY, "")

        tvPass.setOnClickListener {
            showCustomDialog()
        }
        if (!username.isNullOrEmpty()) {
            tvUsername.text = username


            databaseReference.child(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)

                            if (user != null) {
                                tvUsername.text = user.username
                                tvEmail.text = user.email
                                tvPass.text = user.password
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error if any
                    }
                })

            btnLogOut.setOnClickListener {
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                auth.signOut()

                startActivity(Intent(requireContext(), LoginActivity::class.java))

                requireActivity().finish()
            }

        } else {
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.change_pass_dialog)

        val etNewPass = dialog.findViewById<EditText>(R.id.etNewPass)
        val etConfirmPass = dialog.findViewById<EditText>(R.id.etConfirmNewPass)
        val etNowPass = dialog.findViewById<EditText>(R.id.etNowPass)
        val btnChange = dialog.findViewById<Button>(R.id.btnChangePass)

        btnChange.setOnClickListener {
            val newPass = etNewPass.text.toString()
            val confirmPass = etConfirmPass.text.toString()
            val currentPass = etNowPass.text.toString()

            if (newPass.isEmpty() || confirmPass.isEmpty() || currentPass.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val username = sharedPreferences.getString(USERNAME_KEY, "")
            if (username.isNullOrEmpty()) {
                // Handle the case where username is null or empty
                return@setOnClickListener
            }

            // Fetch the current user's password from the Realtime Database
            val userRef = databaseReference.child(username)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val savedPassword = user.password

                            if (savedPassword == currentPass) { // Current password matches
                                if (newPass == confirmPass) { // New password matches confirmation
                                    // Update the password in the Realtime Database
                                    userRef.child("password").setValue(newPass)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Password updated successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Failed to update password",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "New passwords do not match",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Current password is incorrect",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if any
                }
            })
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
