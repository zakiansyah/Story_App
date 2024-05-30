package com.dicoding.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val name: String,
    val isLogin: Boolean = false
)