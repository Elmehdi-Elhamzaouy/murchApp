package com.example.login_system_test01.DataClass

data class Compte (
    val username:String,
    val email:String,
    val password:String,
    var isAdmin:Boolean,
    val image: String?
    )

