package com.example.loginapp.viewmodel

import com.example.loginapp.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) {

    fun authenticate(username: String, password: String): Boolean {
        return userRepository.authenticate(username, password) != null
    }
}
