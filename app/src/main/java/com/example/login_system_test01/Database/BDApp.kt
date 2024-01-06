package com.example.login_system_test01.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.login_system_test01.DataClass.Compte
import com.example.login_system_test01.DataClass.Produits

class BDApp(context: Context) : SQLiteOpenHelper(context, "BD_App", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE Compte (username TEXT PRIMARY KEY, email TEXT, password TEXT, " +
                    "isAdmin BOOLEAN, image TEXT)"
        )
        db.execSQL(
            "CREATE TABLE Produits (image TEXT, prix INT, name TEXT, color1 TEXT, " +
                    "color2 TEXT, color3 TEXT, category TEXT)"
        )
        db.execSQL(
            "CREATE TABLE Panier (username TEXT, image TEXT, prix INT, name TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Compte")
        db.execSQL("DROP TABLE IF EXISTS Produits")
        db.execSQL("DROP TABLE IF EXISTS Panier")
        onCreate(db)
    }

    fun ajouterCompte(compte: Compte): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", compte.username)
            put("email", compte.email)
            put("password", compte.password)
            put("isAdmin", compte.isAdmin)
            put("image", compte.image)
        }

        return db.insert("Compte", null, values)
    }

    fun ajouterProduit(pro: Produits): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("image", pro.image)
            put("prix", pro.prix)
            put("name", pro.name)
            put("color1", pro.color1)
            put("color2", pro.color2)
            put("color3", pro.color3)
            put("category", pro.category)
        }

        return db.insert("Produits", null, values)
    }

    @SuppressLint("Range")
    fun getImagePathByEmail(email: String): String? {
        val db = this.readableDatabase

        val columns = arrayOf("image")
        val selection = "email = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            "Compte",
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var imagePath: String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                imagePath = it.getString(it.getColumnIndex("image"))
            }
        }

        return imagePath
    }

    @SuppressLint("Range")
    fun getEmailByUsernameOrEmail(identifier: String): String? {
        val db = readableDatabase
        val columns = arrayOf("email")
        val selection = "username = ? OR email = ?"
        val selectionArgs = arrayOf(identifier, identifier)

        val cursor = db.query(
            "Compte",
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var email: String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                email = it.getString(it.getColumnIndex("email"))
            }
        }

        return email
    }


    @SuppressLint("Range")
    fun getUsernameByEmailOrUsername(identifier: String): String? {
        val db = readableDatabase
        val columns = arrayOf("username")
        val selection = "email = ? OR username = ?"
        val selectionArgs = arrayOf(identifier, identifier)

        val cursor = db.query(
            "Compte",
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var username: String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                username = it.getString(it.getColumnIndex("username"))
            }
        }

        return username
    }


    fun addImageToAccount(email: String, image: String?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("image", image)
        }

        return db.update("Compte", values, "email = ?", arrayOf(email))
    }

    @SuppressLint("Range")
    fun getAllProducts(): List<Produits> {
        val productsList = mutableListOf<Produits>()
        val db = readableDatabase
        val cursor = db.query(
            "Produits",
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val image = it.getString(it.getColumnIndex("image"))
                val prix = it.getInt(it.getColumnIndex("prix"))
                val name = it.getString(it.getColumnIndex("name"))
                val color1 = it.getString(it.getColumnIndex("color1"))
                val color2 = it.getString(it.getColumnIndex("color2"))
                val color3 = it.getString(it.getColumnIndex("color3"))
                val category = it.getString(it.getColumnIndex("category"))

                val product = Produits(image, prix, name, color1, color2, color3, category)
                productsList.add(product)
            }
        }

        return productsList
    }

    fun checkCredentials(identifier: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            "Compte",
            null,
            "(email = ? OR username = ?) AND password = ?",
            arrayOf(identifier, identifier, password),
            null,
            null,
            null
        )

        val result = cursor.count > 0
        cursor.close()

        // Add logging for debugging
        Log.d("CheckCredentials", "Identifier: $identifier, Password: $password, Result: $result")

        return result
    }

    @SuppressLint("Range")
    fun getAllAccounts(): List<Compte> {
        val accountsList = mutableListOf<Compte>()
        val db = readableDatabase
        val cursor = db.query(
            "Compte",
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val username = it.getString(it.getColumnIndex("username"))
                val email = it.getString(it.getColumnIndex("email"))
                val password = it.getString(it.getColumnIndex("password"))
                val isAdmin = it.getInt(it.getColumnIndex("isAdmin")) == 1

                val account = Compte(username, email, password, isAdmin, null)
                accountsList.add(account)
            }
        }

        return accountsList
    }


    fun addToPanier(username: String, image: String, prix: Int, name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("image", image)
            put("prix", prix)
            put("name", name)
        }

        return db.insert("Panier", null, values)
    }


    @SuppressLint("Range")
    fun getProductsByCategories(categories: List<String>): List<Produits> {
        val productsList = mutableListOf<Produits>()
        val db = readableDatabase

        // Create a selection string with placeholders for each category
        val selection = buildString {
            append("category IN (")
            append(categories.joinToString { "?" })
            append(")")
        }

        val selectionArgs = categories.toTypedArray()

        val cursor = db.query(
            "Produits",
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val image = it.getString(it.getColumnIndex("image"))
                val prix = it.getInt(it.getColumnIndex("prix"))
                val name = it.getString(it.getColumnIndex("name"))
                val color1 = it.getString(it.getColumnIndex("color1"))
                val color2 = it.getString(it.getColumnIndex("color2"))
                val color3 = it.getString(it.getColumnIndex("color3"))
                val category = it.getString(it.getColumnIndex("category"))

                val product = Produits(image, prix, name, color1, color2, color3, category)
                productsList.add(product)
            }
        }

        return productsList
    }

    // Helper function to convert a set of categories to a list
    private fun categoriesToList(selectedCategories: Set<String>): List<String> {
        return selectedCategories.toList()
    }
}
