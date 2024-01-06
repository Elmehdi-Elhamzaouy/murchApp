package com.example.login_system_test01.mainActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_system_test01.DataClass.Compte
import com.example.login_system_test01.Database.BDApp
import com.example.login_system_test01.R

class MainActivity_SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sign_up)

        val signBtn = findViewById<Button>(R.id.signbtn)

        signBtn.setOnClickListener {
            val userEditText = findViewById<EditText>(R.id.user)
            val emailEditText = findViewById<EditText>(R.id.email)
            val passEditText = findViewById<EditText>(R.id.pass)
            val passCoEditText = findViewById<EditText>(R.id.passco)

            val user = userEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val pass = passEditText.text.toString().trim()
            val passCo = passCoEditText.text.toString().trim()

            if (user.isEmpty() || email.isEmpty() || pass.isEmpty() || passCo.isEmpty()) {
                // Display a Toast indicating that all fields must be filled
                Toast.makeText(applicationContext, "All fields must be filled", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate username
            val usernameRegex = "^[a-zA-Z0-9_-]{3,15}\$"
            if (!user.matches(usernameRegex.toRegex())) {
                Toast.makeText(applicationContext, "Invalid username format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate email
            val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            if (!email.matches(emailRegex.toRegex())) {
                Toast.makeText(applicationContext, "Invalid email format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate password
            val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$"
            if (!pass.matches(passwordRegex.toRegex())) {
                Toast.makeText(applicationContext, "Invalid password format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Confirm password
            if (pass != passCo) {
                Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Rest of your code...

            val account = Compte(user, email, pass, false, null)

            val db = BDApp(applicationContext)

            val result = db.ajouterCompte(account)

            if (result != (-1).toLong()) {
                Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
            }
        }

        val haveAcc = findViewById<TextView>(R.id.haveAc)
        haveAcc.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
