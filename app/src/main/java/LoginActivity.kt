package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.repository.UserRepository
import com.example.loginapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo UserRepository và LoginViewModel
        val userRepository = UserRepository()
        loginViewModel = LoginViewModel(userRepository)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Kiểm tra thông tin đăng nhập
            if (loginViewModel.authenticate(username, password)) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            } else {
                // Hiển thị thông báo lỗi nếu thông tin đăng nhập không chính xác
                usernameEditText.error = "Invalid username or password"
                passwordEditText.error = "Invalid username or password"
            }
        }
    }
}
