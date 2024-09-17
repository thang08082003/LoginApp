import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.loginapp.R
import com.example.loginapp.model.User
import com.example.loginapp.repository.UserRepository
import java.util.*

class EditUserHandler(
    private val context: Context,
    private val userRepository: UserRepository
) {

    fun showEditUserDialog() {
        val dialogView =
            (context as AppCompatActivity).layoutInflater.inflate(R.layout.dialog_search_user, null)
        val searchEditText = dialogView.findViewById<EditText>(R.id.editTextSearchUsername)
        val listView = dialogView.findViewById<ListView>(R.id.listViewUsers)

        val users = userRepository.getAllUsers()

        val adapter = object : ArrayAdapter<User>(context, R.layout.list_item_user, users) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.list_item_user, parent, false)
                val user = getItem(position)
                val textViewUsername = view.findViewById<TextView>(R.id.textViewUsername)
                val imageButtonOptions = view.findViewById<ImageButton>(R.id.imageButtonOptions)

                textViewUsername.text = user?.username

                imageButtonOptions.setOnClickListener {
                    showUserOptionsMenu(it, user!!)
                }

                return view
            }
        }

        listView.adapter = adapter

        searchEditText.addTextChangedListener { text ->
            val searchTerm = text.toString()
            val filteredUsers = users.filter { it.username.contains(searchTerm, ignoreCase = true) }
            adapter.clear()
            adapter.addAll(filteredUsers)
            adapter.notifyDataSetChanged()
        }

        AlertDialog.Builder(context)
            .setTitle("Search User")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showUserOptionsMenu(view: View, user: User) {
        val popupMenu = PopupMenu(context, view)
        val menuInflater = popupMenu.menuInflater
        menuInflater.inflate(R.menu.user_options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_edit -> showEditUserDetailsDialog(user)
                R.id.action_delete -> confirmDeleteUser(user)
            }
            true
        }

        popupMenu.show()
    }

    private fun showEditUserDetailsDialog(user: User) {
        val dialogView =
            (context as AppCompatActivity).layoutInflater.inflate(R.layout.dialog_edit_user, null)
        val newUsernameEditText = dialogView.findViewById<EditText>(R.id.editTextNewUsername)
        val newPasswordEditText = dialogView.findViewById<EditText>(R.id.editTextNewPassword)
        val newFullNameEditText = dialogView.findViewById<EditText>(R.id.editTextNewFullName)
        val newEmailEditText = dialogView.findViewById<EditText>(R.id.editTextNewEmail)
        val newPhoneEditText = dialogView.findViewById<EditText>(R.id.editTextNewPhone)
        val genderSpinner = dialogView.findViewById<Spinner>(R.id.spinnerGender)
        val birthDateEditText = dialogView.findViewById<EditText>(R.id.editTextNewBirth)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        // Set current user data into the fields
        newUsernameEditText.setText(user.username)
        newPasswordEditText.setText(user.password)
        newFullNameEditText.setText(user.fullname)
        newEmailEditText.setText(user.email)
        newPhoneEditText.setText(user.phone)
        birthDateEditText.setText(user.birth)

        // Set up gender spinner
        val genders = listOf("Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, genders)
        genderSpinner.adapter = genderAdapter
        genderSpinner.setSelection(genders.indexOf(user.gender))

        // Set up birth date picker
        birthDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    birthDateEditText.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("Edit User")
            .setView(dialogView)
            .create()

        dialog.setOnShowListener {
            btnSave.setOnClickListener {
                val newUsername = newUsernameEditText.text.toString()
                val newPassword = newPasswordEditText.text.toString()
                val newFullName = newFullNameEditText.text.toString()
                val newEmail = newEmailEditText.text.toString()
                val newPhone = newPhoneEditText.text.toString()
                val newGender = genderSpinner.selectedItem.toString()
                val newBirth = birthDateEditText.text.toString()

                if (newUsername.isNotEmpty() && newPassword.isNotEmpty() && newFullName.isNotEmpty() && newEmail.isNotEmpty() && newPhone.isNotEmpty() && newBirth.isNotEmpty()) {
                    val existingUser = userRepository.getAllUsers()
                        .find { it.username == newUsername && it.username != user.username }
                    if (existingUser != null) {
                        Toast.makeText(
                            context,
                            "Username already exists. Please choose another.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (userRepository.editUser(
                                user.username,
                                newUsername,
                                newPassword,
                                newFullName,
                                newEmail,
                                newGender,
                                newPhone,
                                newBirth
                            )
                        ) {
                            Toast.makeText(context, "User edited successfully", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(context, "Error updating user", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter valid details", Toast.LENGTH_SHORT).show()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun confirmDeleteUser(user: User) {
        AlertDialog.Builder(context)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this user?")
            .setPositiveButton("Delete") { _, _ ->
                if (userRepository.deleteUser(user.username)) {
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
