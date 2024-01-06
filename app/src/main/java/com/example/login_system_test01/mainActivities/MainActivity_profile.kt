package com.example.login_system_test01.mainActivities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_system_test01.Database.BDApp
import com.example.login_system_test01.R
import com.squareup.picasso.Picasso

class MainActivity_profile : AppCompatActivity() {

    private lateinit var userEmail: String
    private lateinit var userEmailTextView: TextView
    private lateinit var profImgImageView: ImageView
    private lateinit var editImgImageView: ImageView
    private lateinit var logOut: Button
    private lateinit var db: BDApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_profile)

        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        db = BDApp(applicationContext)

        val result = db.getEmailByUsernameOrEmail(userEmail) ?: ""

        userEmailTextView = findViewById(R.id.email)
        profImgImageView = findViewById(R.id.prof_img)
        editImgImageView = findViewById(R.id.edit_img)
        logOut = findViewById(R.id.logout)

        logOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        userEmailTextView.text = result

        val initialImagePath = db.getImagePathByEmail(result) ?: ""

        Picasso.get().load(initialImagePath).placeholder(R.drawable.default_pfp).into(profImgImageView)

        // Set onClickListener for edit_img ImageView
        editImgImageView.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Image Link")

            val input = EditText(this)
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ ->
                val link = input.text.toString().trim()

                // Insert the link into the "Compte" table
                val affectedRows = db.addImageToAccount(result, link)

                if (affectedRows > 0) {
                    Toast.makeText(this, "Image link added successfully", Toast.LENGTH_SHORT).show()

                    Picasso.get().load(initialImagePath).placeholder(R.drawable.default_pfp).into(profImgImageView)
                } else {
                    Toast.makeText(this, "Failed to add image link", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }

    }
}