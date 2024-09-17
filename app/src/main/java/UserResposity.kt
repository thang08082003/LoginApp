package com.example.loginapp.repository

import com.example.loginapp.model.User

class UserRepository {

    private val users = mutableListOf(
        User("thang", "1234", "Thang Nguyen", "thang@example.com", "Male", "0123456789", "01/01/1990")
    )

    fun authenticate(username: String, password: String): User? {
        return users.find { it.username == username && it.password == password }
    }

    fun addUser(
        username: String,
        password: String,
        fullName: String,
        email: String,
        gender: String,
        phone: String,
        birth: String
    ): Boolean {
        return if (users.any { it.username == username }) {
            false
        } else {
            users.add(User(username, password, fullName, email, gender, phone, birth))
            true
        }
    }

    fun editUser(
        username: String,
        newUsername: String,
        newPassword: String,
        newFullName: String,
        newEmail: String,
        newGender: String,
        newPhone: String,
        newBirth: String
    ): Boolean {
        val user = users.find { it.username == username }
        return if (user != null) {
            user.username = newUsername
            user.password = newPassword
            user.fullname = newFullName
            user.email = newEmail
            user.gender = newGender
            user.phone = newPhone
            user.birth = newBirth
            true
        } else {
            false
        }
    }

    fun deleteUser(username: String): Boolean {
        val user = users.find { it.username == username }
        return if (user != null) {
            users.remove(user)
            true
        } else {
            false
        }
    }

    fun getAllUsers(): List<User> {
        return users
    }
}

