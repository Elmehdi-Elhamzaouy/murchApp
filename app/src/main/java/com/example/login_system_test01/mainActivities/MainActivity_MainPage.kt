package com.example.login_system_test01.mainActivities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.login_system_test01.R

class MainActivity_MainPage : AppCompatActivity() {

    private lateinit var userEmail: String
    private lateinit var catbtn: Button

    private var backButtonPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_main_page)

        catbtn = findViewById(R.id.catbtn)
        catbtn.setOnClickListener {
            val intent = Intent(this, Categorie_and_search::class.java)
            intent.putExtra("USER_EMAIL", userEmail)
            startActivity(intent)
        }

        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        val prflbtn = findViewById<Button>(R.id.prflbtn)
        prflbtn.setOnClickListener {
            val intent = Intent(this, MainActivity_profile::class.java)
            intent.putExtra("USER_EMAIL", userEmail)
            startActivity(intent)
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (backButtonPressedOnce) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to close the app?")
            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish() // Close the app
            }
            builder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            builder.show()
        } else {
            backButtonPressedOnce = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

            // Reset the flag after a certain duration
            android.os.Handler().postDelayed({
                backButtonPressedOnce = false
            }, 2000) // Adjust the duration as needed
        }
    }
}