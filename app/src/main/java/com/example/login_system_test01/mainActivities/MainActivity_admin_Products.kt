package com.example.login_system_test01.mainActivities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.GridView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.login_system_test01.Database.BDApp
import com.example.login_system_test01.Adapters.ProductAdapter
import com.example.login_system_test01.R

class MainActivity_admin_Products : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var db: BDApp
    private lateinit var menToggleButton: ToggleButton
    private lateinit var womenToggleButton: ToggleButton
    private lateinit var unisexToggleButton: ToggleButton


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin_products)

        gridView = findViewById(R.id.pro)

        // Initialize your database helper
        db = BDApp(this)

        // Initial load - Display all products
        loadProducts("Men","Women","Unisex")

        // Set click listeners for the toggle buttons
        menToggleButton = findViewById(R.id.men)
        womenToggleButton = findViewById(R.id.women)
        unisexToggleButton = findViewById(R.id.unisex)

        menToggleButton.setOnCheckedChangeListener { _, isChecked -> onToggleButtonCheckedChanged(isChecked) }
        womenToggleButton.setOnCheckedChangeListener { _, isChecked -> onToggleButtonCheckedChanged(isChecked) }
        unisexToggleButton.setOnCheckedChangeListener { _, isChecked -> onToggleButtonCheckedChanged(isChecked) }
    }

    private fun onToggleButtonCheckedChanged(isChecked: Boolean) {

        if (menToggleButton.isChecked){
            womenToggleButton.isChecked=false
            unisexToggleButton.isChecked=false
            loadProducts("Men")
        }
        else if (womenToggleButton.isChecked){
            menToggleButton.isChecked  = false
            unisexToggleButton.isChecked = false
            loadProducts("Women")
        }
        else if (unisexToggleButton.isChecked){
            menToggleButton.isChecked  = false
            womenToggleButton.isChecked = false
            loadProducts("Unisex")
        }


        else {
            loadProducts("Men", "Women","Unisex")
        }
    }

    private fun loadProducts(vararg categories: String) {
        // Get the product list from the database based on the selected categories
        val productList = if (categories.isNotEmpty()) {
            db.getProductsByCategories(categories.toList())
        } else {
            db.getAllProducts()
        }



        // Create and set the adapter
        val productAdapter = ProductAdapter(this, productList)
        gridView.adapter = productAdapter
    }
}
