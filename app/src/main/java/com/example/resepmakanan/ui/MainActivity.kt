package com.example.resepmakanan.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.resepmakanan.R
import com.example.resepmakanan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Inflate layout pakai ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // ✅ Ambil NavHostFragment dengan safe-cast
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found. Pastikan id=navHostFragment ada di activity_main.xml")

        // ✅ Ambil NavController dari NavHostFragment
        navController = navHostFragment.navController

        // ✅ Daftar fragment yang ada di bottom nav
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.recipesFragment,
                R.id.favoriteRecipesFragment,
                R.id.foodJokeFragment

            )
        )

        // ✅ Hubungkan BottomNavigationView dengan NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // ✅ Hubungkan ActionBar (judul toolbar) dengan NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // ✅ Handle tombol back di AppBar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}