package com.example.resepmakanan.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.resepmakanan.R
import androidx.navigation.NavOptions


class ProfileFragment : Fragment() {

    private lateinit var tvProfileNameTop: TextView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPassword: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi view
        tvProfileNameTop = view.findViewById(R.id.tvProfileNameTop)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPassword = view.findViewById(R.id.tvPassword)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Ambil data dari SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")
        val email = sharedPref.getString("email", "user@example.com")
        val password = sharedPref.getString("password", "••••••••")

        // Tampilkan data
        tvProfileNameTop.text = username
        tvName.text = username
        tvEmail.text = email
        tvPassword.text = password?.replace(Regex("."), "•")

        // Tombol Edit Profile
        btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            // Hapus session
            sharedPref.edit().clear().apply()

            // Navigasi ke LoginFragment dan clear backstack
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.my_nav, true) // gunakan id dari root <navigation>
                .build()

            findNavController().navigate(
                R.id.action_profileFragment_to_loginFragment,
                null,
                navOptions
            )
        }


        return view
    }
}
