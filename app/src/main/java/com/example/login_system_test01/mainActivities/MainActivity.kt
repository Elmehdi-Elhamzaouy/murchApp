package com.example.login_system_test01.mainActivities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.login_system_test01.DataClass.Compte
import com.example.login_system_test01.Database.BDApp
import com.example.login_system_test01.R

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var logBtn: Button

    private var backButtonPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bdLogin = BDApp(this)

        // Create a new account
        val newAccount = Compte("admin", "admin@gmail.com", "admin123", true, null)

        // Insert the new account into the database
        val result = bdLogin.ajouterCompte(newAccount)

        val signUp = findViewById<TextView>(R.id.signPage)
        emailEditText = findViewById(R.id.email)
        passEditText = findViewById(R.id.pass)
        logBtn = findViewById(R.id.logbtn)

        signUp.setOnClickListener {
            val intent = Intent(this, MainActivity_SignUp::class.java)
            startActivity(intent)
        }

        // Assuming you have a reference to your database helper
        val dbHelper = BDApp(this)

        // Create a handler for delayed execution
        val handler = Handler()

        // Perform the action when the login button is clicked
        logBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val pass = passEditText.text.toString()

            // Show loading indicator on the button
            logBtn.text = "Loading..."
            logBtn.isEnabled = false // Disable the button during the delay

            // Introduce a delay using a Handler
            handler.postDelayed({
                // Check if the entered credentials exist in the database
                val isCredentialsValid = dbHelper.checkCredentials(email, pass)

                // Reset button text and enable it after the delay
                logBtn.text = "Login"
                logBtn.isEnabled = true

                if (isCredentialsValid) {
                    // Retrieve the account information
                    val account = dbHelper.getAllAccounts().find { it.email == email }

                    if (account != null && account.isAdmin) {
                        // Perform your action for admin accounts here
                        Toast.makeText(applicationContext, "Admin logged in!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity_Admin::class.java)
                        startActivity(intent)
                    } else {
                        // Perform your action for regular users here
                        Toast.makeText(applicationContext, "User logged in!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity_MainPage::class.java)
                        intent.putExtra("USER_EMAIL", email)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_LONG).show()
                }
            }, 2000) // 2000 milliseconds (2 seconds) delay, adjust as needed
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
