package com.example.login_system_test01.mainActivities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_system_test01.Database.BDApp
import com.example.login_system_test01.DataClass.Produits
import com.example.login_system_test01.R

class MainActivity_AddPro : AppCompatActivity() {

    private lateinit var imgEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var color1EditText: EditText
    private lateinit var color2EditText: EditText
    private lateinit var color3EditText: EditText
    private lateinit var addProButton: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var db: BDApp

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_add_pro)

        imgEditText = findViewById(R.id.img)
        priceEditText = findViewById(R.id.price)
        nameEditText = findViewById(R.id.name)
        color1EditText = findViewById(R.id.color1)
        color2EditText = findViewById(R.id.color2)
        color3EditText = findViewById(R.id.color3)
        addProButton = findViewById(R.id.addPro)
        categorySpinner = findViewById(R.id.categorySpinner)


        // Initialize your database helper
        db = BDApp(this)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        )



        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        categorySpinner.adapter = adapter


        addProButton.setOnClickListener {
            val image = imgEditText.text.toString().trim()
            val priceText = priceEditText.text.toString().trim()
            val productName = nameEditText.text.toString().trim()
            val color1 = color1EditText.text.toString().trim()
            val color2 = color2EditText.text.toString().trim()
            val color3 = color3EditText.text.toString().trim()

            // Check if any of the required fields is empty
            if (image.isEmpty() || priceText.isEmpty() || productName.isEmpty() || color1.isEmpty() || color2.isEmpty() || color3.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill in all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate and parse the price
            val price = priceText.toIntOrNull()
            if (price == null || price <= 0) {
                Toast.makeText(applicationContext, "Invalid price", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val category = categorySpinner.selectedItem.toString()

            // Create a Produits object with the entered values
            val product = Produits(image, price, productName, color1, color2, color3, category)

            // Add the product to the database
            val result = db.ajouterProduit(product)

            if (result != -1L) {
                Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity_Admin::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
            }
        }
    }
}


