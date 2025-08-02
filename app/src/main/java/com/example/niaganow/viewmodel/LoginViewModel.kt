package com.example.niaganow.viewmodel

import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    fun validateSignIn(email: String, password: String): Boolean {
        return email.contains("@") && password.length >= 6
    }

    fun validateSignUp(email: String, password: String): Boolean {
        return email.contains("@") && password.length >= 6
    }
}