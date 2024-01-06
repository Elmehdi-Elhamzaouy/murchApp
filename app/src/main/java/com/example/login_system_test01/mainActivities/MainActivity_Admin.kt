package com.example.login_system_test01.mainActivities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.login_system_test01.R

class MainActivity_Admin : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)


        val add_btn = findViewById<Button>(R.id.add_btn)

        add_btn.setOnClickListener {
            val intent = Intent(this, MainActivity_AddPro::class.java)
            startActivity(intent)
        }

        val show_btn = findViewById<Button>(R.id.show_btn)

        show_btn.setOnClickListener {
            val intent = Intent(this, MainActivity_admin_Products::class.java)
            startActivity(intent)
        }
    }
}