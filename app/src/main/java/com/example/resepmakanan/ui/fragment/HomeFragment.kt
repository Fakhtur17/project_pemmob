package com.example.resepmakanan.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.resepmakanan.DetailActivity
import com.example.resepmakanan.R
import com.example.resepmakanan.adapters.CategoryAdapter
import com.example.resepmakanan.adapters.LatestRecipeAdapter
import com.example.resepmakanan.adapters.SliderAdapter
import com.example.resepmakanan.models.Category
import com.example.resepmakanan.models.Result
import com.example.resepmakanan.util.Constants
import com.example.resepmakanan.util.NetworkResult
import com.example.resepmakanan.viewmodels.MainViewModel
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var latestRecipeAdapter: LatestRecipeAdapter

    private lateinit var vpPopular: ViewPager2
    private lateinit var dotsIndicator: WormDotsIndicator
    private lateinit var rvCategory: RecyclerView
    private lateinit var rvLatest: RecyclerView
    private lateinit var searchBar: CardView
    private lateinit var etSearch: EditText
    private lateinit var tvGreeting: TextView
    private lateinit var btnProfile: ImageView

    private val sliderHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🧩 Inisialisasi View
        vpPopular = view.findViewById(R.id.vpPopular)
        dotsIndicator = view.findViewById(R.id.dotsIndicator)
        rvCategory = view.findViewById(R.id.rvCategory)
        rvLatest = view.findViewById(R.id.rvLatest)
        searchBar = view.findViewById(R.id.searchBar)
        etSearch = view.findViewById(R.id.etSearch)
        tvGreeting = view.findViewById(R.id.tvGreeting)
        btnProfile = view.findViewById(R.id.btnProfile)

        // 🔹 Ambil nama user dari SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")
        tvGreeting.text = "Hallo, $username 👋"

        // 🔹 Tombol profil
        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        // 🔹 Layout RecyclerView
        rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvLatest.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // 🔍 Fitur pencarian
        searchBar.setOnClickListener {
            val keyword = etSearch.text.toString()
            if (keyword.isNotEmpty()) {
                Toast.makeText(requireContext(), "Cari resep: $keyword 🍽️", Toast.LENGTH_SHORT).show()
                // nanti bisa diarahkan ke fragment hasil pencarian
            } else {
                Toast.makeText(requireContext(), "Masukkan kata kunci pencarian", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 Observasi data dari ViewModel
        observePopularRecipes()
        loadPopularRecipes()
    }

    private fun observePopularRecipes() {
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val results = response.data?.results
                    if (!results.isNullOrEmpty()) {
                        setupPopularSlider(results)
                        setupCategoriesFromRecipes(results)
                        setupLatestRecipes(results)
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    // bisa tambahkan shimmer/loading animation
                }
            }
        }
    }

    private fun loadPopularRecipes() {
        val queries = hashMapOf(
            "number" to "10",
            "apiKey" to Constants.API_KEY,
            "addRecipeInformation" to "true",
            "fillIngredients" to "true"
        )
        mainViewModel.getRecipes(queries)
    }

    /**
     * 🔹 Setup ViewPager (Popular Recipes)
     */
    private fun setupPopularSlider(results: List<Result>) {
        sliderAdapter = SliderAdapter(results) { recipe ->
            // ✅ Klik item slider → buka DetailActivity
            DetailActivity.start(requireActivity() as AppCompatActivity, recipe)
        }
        vpPopular.adapter = sliderAdapter
        dotsIndicator.attachTo(vpPopular)
        startAutoSlide(results.size)
    }

    /**
     * 🔹 Setup kategori dinamis
     */
    private fun setupCategoriesFromRecipes(results: List<Result>) {
        val categories = mutableListOf<Category>()

        if (results.any { it.vegetarian })
            categories.add(Category("Vegetarian", "https://img.icons8.com/color/96/vegetarian-food-symbol.png"))
        if (results.any { it.vegan })
            categories.add(Category("Vegan", "https://img.icons8.com/color/96/vegan-symbol.png"))
        if (results.any { it.glutenFree })
            categories.add(Category("Gluten Free", "https://img.icons8.com/color/96/no-gluten.png"))
        if (results.any { it.dairyFree })
            categories.add(Category("Dairy Free", "https://img.icons8.com/color/96/no-milk.png"))

        categoryAdapter = CategoryAdapter(categories) { category ->
            val bundle = Bundle()
            bundle.putString("category", category.name)
            findNavController().navigate(R.id.action_homeFragment_to_recipesFragment, bundle)
        }

        rvCategory.setHasFixedSize(true)
        rvCategory.adapter = categoryAdapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvCategory)
    }

    /**
     * 🔹 Setup daftar resep terbaru
     */
    private fun setupLatestRecipes(results: List<Result>) {
        latestRecipeAdapter = LatestRecipeAdapter(results) { recipe ->
            // ✅ Klik item resep → buka DetailActivity
            DetailActivity.start(requireActivity() as AppCompatActivity, recipe)
        }
        rvLatest.adapter = latestRecipeAdapter
    }

    private fun startAutoSlide(size: Int) {
        val runnable = object : Runnable {
            override fun run() {
                if (size > 0) {
                    val nextItem = (vpPopular.currentItem + 1) % size
                    vpPopular.setCurrentItem(nextItem, true)
                    sliderHandler.postDelayed(this, 5000)
                }
            }
        }
        sliderHandler.postDelayed(runnable, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacksAndMessages(null)
    }
}
