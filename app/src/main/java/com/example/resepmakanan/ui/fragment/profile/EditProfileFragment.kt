package com.example.resepmakanan.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.resepmakanan.R

class EditProfileFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        etUsername = view.findViewById(R.id.etUsername)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)

        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val email = sharedPref.getString("email", "")
        val password = sharedPref.getString("password", "")

        etUsername.setText(username)
        etEmail.setText(email)
        etPassword.setText(password)

        btnSave.setOnClickListener {
            val newUsername = etUsername.text.toString()
            val newEmail = etEmail.text.toString()
            val newPassword = etPassword.text.toString()

            with(sharedPref.edit()) {
                putString("username", newUsername)
                putString("email", newEmail)
                putString("password", newPassword)
                apply()
            }

            findNavController().navigateUp() // kembali ke ProfileFragment
        }

        btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }
}
