package com.dicoding.ui.signup

import androidx.lifecycle.ViewModel
import com.dicoding.data.UserRepository

class SignupViewModel(private val repository: UserRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = repository.signup(name, email, password)
}