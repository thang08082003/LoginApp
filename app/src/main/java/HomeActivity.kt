package com.example.loginapp


import EditUserHandler
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.repository.UserRepository

class HomeActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var addUserHandler: AddUserHandler
    private lateinit var editUserHandler: EditUserHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userRepository = UserRepository()
        addUserHandler = AddUserHandler(this, userRepository)
        editUserHandler = EditUserHandler(this, userRepository)

        val welcomeMessage = findViewById<TextView>(R.id.welcome_message)
        val username = intent.getStringExtra("username")
        welcomeMessage.text = "Hello, Welcome $username"

        val btnAddUser: Button = findViewById(R.id.btn_add_user)
        val btnEditUser: Button = findViewById(R.id.btn_edit_user)

        btnAddUser.setOnClickListener {
            addUserHandler.showAddUserDialog()
        }

        btnEditUser.setOnClickListener {
            editUserHandler.showEditUserDialog()
        }
    }
}
