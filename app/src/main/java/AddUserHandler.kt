package com.example.loginapp

import android.app.DatePickerDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.repository.UserRepository
import java.util.Calendar

class AddUserHandler(
    private val context: Context,
    private val userRepository: UserRepository
) {

    fun showAddUserDialog() {
        val dialogView = (context as AppCompatActivity).layoutInflater.inflate(R.layout.dialog_add_user, null)
        val usernameEditText = dialogView.findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = dialogView.findViewById<EditText>(R.id.editTextPassword)
        val fullNameEditText = dialogView.findViewById<EditText>(R.id.editTextFullName)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val genderSpinner = dialogView.findViewById<Spinner>(R.id.spinnerGender)
        val phoneEditText = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val birthEditText = dialogView.findViewById<EditText>(R.id.editTextBirth)
        val btnReset = dialogView.findViewById<Button>(R.id.btnReset)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)

        // Khởi tạo Spinner cho giới tính
        val genders = arrayOf("Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, genders)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Add User")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()

        btnAdd.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val gender = genderSpinner.selectedItem.toString() // Lấy giá trị giới tính từ Spinner
            val phone = phoneEditText.text.toString()
            val birth = birthEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && birth.isNotEmpty()) {
                if (userRepository.addUser(username, password, fullName, email, gender, phone, birth)) {
                    Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show()
            }
        }

        btnReset.setOnClickListener {
            usernameEditText.text.clear()
            passwordEditText.text.clear()
            fullNameEditText.text.clear()
            emailEditText.text.clear()
            phoneEditText.text.clear()
            birthEditText.text.clear()
        }

        // Thiết lập DatePicker cho ngày sinh
        birthEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    birthEditText.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        alertDialog.show()
    }
}
