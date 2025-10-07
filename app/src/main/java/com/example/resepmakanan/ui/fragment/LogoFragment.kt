package com.example.resepmakanan.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.resepmakanan.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logo, container, false)

        val logo = view.findViewById<View>(R.id.imgLogo)
        val appName = view.findViewById<View>(R.id.txtAppName)
        val slogan = view.findViewById<View>(R.id.txtSlogan)

        lifecycleScope.launch {
            // 🔹 Animasi fade-in bertahap
            logo.animate().alpha(1f).setDuration(1000).start()
            delay(800)
            appName.animate().alpha(1f).setDuration(800).start()
            delay(500)
            slogan.animate().alpha(1f).setDuration(800).start()

            // 🔹 Tunggu semua animasi selesai
            delay(1500)

            // 🔹 Navigasi ke LoginFragment
            findNavController().navigate(R.id.action_logoFragment_to_loginFragment)
        }

        return view
    }
}
