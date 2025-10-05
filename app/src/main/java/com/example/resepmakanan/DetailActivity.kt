package com.example.resepmakanan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.resepmakanan.databinding.ActivityDetailBinding
import com.example.resepmakanan.models.Result
import com.example.resepmakanan.ui.fragment.detail.DetailPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var result: Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data recipe dari intent
        result = intent.getParcelableExtra("recipe")!!

        // Atur toolbar
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Setup ViewPager + TabLayout
        val pagerAdapter = DetailPagerAdapter(this, result)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Ingredients"
                else -> "Instructions"
            }
        }.attach()
    }

    companion object {
        // Fungsi pembuka DetailActivity (bisa dipanggil dari mana saja)
        fun start(activity: AppCompatActivity, recipe: Result) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("recipe", recipe)
            activity.startActivity(intent)
        }
    }
}
