package com.example.mobliefinal

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.mobliefinal.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

    class MainActivity : AppCompatActivity() {
        lateinit var binding: ActivityMainBinding
        lateinit var btnAddTopic: Button
        lateinit var btnAddFolder: Button

        companion object {
        const val USER_PREFS = "userPrefs"
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            replaceFragment(FragmentHome())

//            val toolbar: Toolbar = findViewById(R.id.toolbar)
//            setSupportActionBar(toolbar)
//            supportActionBar?.setDisplayShowTitleEnabled(false)

            binding.bottomNavigation.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nav_home -> replaceFragment(FragmentHome())
                    R.id.nav_profile -> replaceFragment(FragmentProfile())
                    R.id.nav_add -> showCustomDialog()
                    R.id.nav_libary -> replaceFragment(FragmentLibrary())
                    R.id.nav_solution -> replaceFragment(FragmentTopic())
                    else -> {

                    }
                }
                true
            }
            if (isUserLoggedIn()) {
                showFragmentMain()
            } else {
                showLoginActivity()
            }
        }
//        override fun onCreateOptionsMenu(menu: Menu): Boolean {
//            val inflater: MenuInflater = menuInflater
//            inflater.inflate(R.menu.library, menu) // R.menu.menu_main là file menu của bạn
//            return true
//        }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }


        private fun showCustomDialog() {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.custom_dialog) // Thay thế bằng layout dialog của bạn

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.gravity = Gravity.BOTTOM
            dialog.window?.attributes = layoutParams


            btnAddTopic = dialog.findViewById<Button>(R.id.btnAddTopic)
            btnAddFolder = dialog.findViewById(R.id.btnAddFolder)

            dialog.show()

            btnAddTopic.setOnClickListener{
                val intent = Intent(this@MainActivity, AddTopicActivity::class.java)
                startActivity(intent)
            }

            btnAddFolder.setOnClickListener{
                val intent = Intent(this@MainActivity, AddFolderActivity::class.java)
                startActivity(intent)
            }


        }


        private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null) != null
    }

        private fun showFragmentMain() {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FragmentMain())
                .commit()
        }


    private fun showLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Đặt finish để ngăn người dùng quay lại MainActivity từ LoginActivity
    }

}