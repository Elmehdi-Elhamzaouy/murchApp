package com.example.login_system_test01.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.login_system_test01.Database.BDApp
import com.example.login_system_test01.DataClass.Produits
import com.example.login_system_test01.R
import com.squareup.picasso.Picasso

class ProductAdapter_2(
    private val context: Context,
    private val productList: List<Produits>,
    private val usernameOrEmail: String
)
    :BaseAdapter() {

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, null)

        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productColorTextView1: View = view.findViewById(R.id.color1)
        val productColorTextView2: View = view.findViewById(R.id.color2)
        val productColorTextView3: View = view.findViewById(R.id.color3)
        val btnPanier: Button = view.findViewById(R.id.panier)

        val product = productList[position]

        // Load image from URL using Picasso
        Picasso.get().load(product.image).placeholder(R.drawable.rectangle_7).into(productImage)

        productName.text = product.name
        productPrice.text = "${product.prix} MAD"

        // Convert color Strings to Int
        val colorInt1 = Color.parseColor(product.color1)
        val colorInt2 = Color.parseColor(product.color2)
        val colorInt3 = Color.parseColor(product.color3)

        // Create ShapeDrawables with circular shapes and solid colors
        val shapeDrawable1 = ShapeDrawable(OvalShape())
        shapeDrawable1.paint.color = colorInt1
        productColorTextView1.background = shapeDrawable1

        val shapeDrawable2 = ShapeDrawable(OvalShape())
        shapeDrawable2.paint.color = colorInt2
        productColorTextView2.background = shapeDrawable2

        val shapeDrawable3 = ShapeDrawable(OvalShape())
        shapeDrawable3.paint.color = colorInt3
        productColorTextView3.background = shapeDrawable3

        btnPanier.setOnClickListener {
            addToPanier(usernameOrEmail, product)
        }

        return view
    }

    private fun addToPanier(username: String, product: Produits) {
        val bdApp = BDApp(context)

        val result = bdApp.addToPanier(username, product.image, product.prix, product.name)

        if (username.isNotEmpty()) {
            if (result != -1L) {
                // Product added to Panier successfully
                Toast.makeText(context, "Product added to Panier", Toast.LENGTH_SHORT).show()
            } else {
                // Failed to add product to Panier
                Toast.makeText(context, "Failed to add product to Panier", Toast.LENGTH_SHORT)
                    .show()
            }
        }else {
            Toast.makeText(context, "Error adding the product", Toast.LENGTH_SHORT).show()
        }
    }
}

