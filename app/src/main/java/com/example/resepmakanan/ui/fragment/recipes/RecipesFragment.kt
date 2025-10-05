package com.example.resepmakanan.ui.fragment.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resepmakanan.R
import com.example.resepmakanan.adapters.RecipesAdapter
import com.example.resepmakanan.util.NetworkResult
import com.example.resepmakanan.viewmodels.MainViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import com.example.resepmakanan.util.Constants
import android.content.Intent
import com.example.resepmakanan.DetailActivity

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: RecipesAdapter
    private lateinit var recyclerView: ShimmerRecyclerView
    private lateinit var errorImageView: ImageView
    private lateinit var errorTextView: TextView
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)

        // 🔹 Inisialisasi View
        recyclerView = view.findViewById(R.id.recyclerView)
        errorImageView = view.findViewById(R.id.error_imageView)
        errorTextView = view.findViewById(R.id.error_textView)
        fab = view.findViewById(R.id.recipes_fab)

        // 🔹 Setup RecyclerView + Adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecipesAdapter()
        recyclerView.adapter = adapter

        // ✅ Tambahkan listener klik item
        adapter.setOnItemClickListener { recipe ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("recipe", recipe)
            startActivity(intent)
        }

        // 🔹 Observasi data dari ViewModel
        observeData()

        // 🔹 Load data pertama kali
        loadRecipes()

        // 🔹 Tombol Refresh
        fab.setOnClickListener {
            val bottomSheet = FilterBottomSheetFragment { selectedFilter ->
                loadRecipes(selectedFilter)
            }
            bottomSheet.show(parentFragmentManager, "FilterBottomSheet")
        }


        return view
    }

    /**
     * Observasi hasil LiveData dari ViewModel
     */
    private fun observeData() {
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    recyclerView.hideShimmer()
                    recyclerView.visibility = View.VISIBLE

                    val foodRecipe = response.data

                    if (foodRecipe?.results.isNullOrEmpty()) {
                        // ❌ Kosong → tampilkan pesan error
                        recyclerView.visibility = View.GONE
                        errorImageView.visibility = View.VISIBLE
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.text = "Resep tidak ditemukan"
                    } else {
                        // ✅ Ada data → tampilkan di RecyclerView
                        errorImageView.visibility = View.GONE
                        errorTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter.setData(foodRecipe.results)
                    }
                }

                is NetworkResult.Error -> {
                    recyclerView.hideShimmer()
                    recyclerView.visibility = View.GONE
                    errorImageView.visibility = View.VISIBLE
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text = response.message ?: "Terjadi kesalahan"
                    Log.e("API_DEBUG", "Error: ${response.message}")
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    // ✅ Saat loading, tampilkan shimmer
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.showShimmer()
                    errorImageView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Fungsi untuk memanggil API
     */
    private fun loadRecipes(filter: String? = null) {
        val queries = hashMapOf(
            "number" to "10",
            "apiKey" to Constants.API_KEY,
            "addRecipeInformation" to "true",
            "fillIngredients" to "true"
        )

        // 🔹 Kalau ada filter, tambahkan sesuai pilihannya
        when (filter) {
            "vegetarian" -> queries["diet"] = "vegetarian"
            "vegan" -> queries["diet"] = "vegan"
            "drink" -> queries["type"] = "drink"
            "main course" -> queries["type"] = "main course"
            else -> {} // "Semua" -> gak usah nambah filter
        }

        mainViewModel.getRecipes(queries)
    }

}
