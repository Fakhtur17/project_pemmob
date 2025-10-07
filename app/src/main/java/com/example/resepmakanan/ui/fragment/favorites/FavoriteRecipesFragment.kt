package com.example.resepmakanan.ui.fragment.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resepmakanan.adapters.RecipesAdapter
import com.example.resepmakanan.databinding.FragmentFavoriteRecipesBinding
import com.example.resepmakanan.util.FavoriteManager
import com.example.resepmakanan.DetailActivity
import androidx.core.view.isVisible
import android.widget.Toast

class FavoriteRecipesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteRecipesBinding
    private lateinit var adapter: RecipesAdapter
    private lateinit var favoriteManager: FavoriteManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        favoriteManager = FavoriteManager(requireContext())

        adapter = RecipesAdapter()
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecyclerView.adapter = adapter

        // 🔹 Klik item favorit → buka DetailActivity
        adapter.setOnItemClickListener { recipe ->
            DetailActivity.start(requireActivity() as androidx.appcompat.app.AppCompatActivity, recipe)
        }

        // 🔹 Klik tombol hapus favorit
        adapter.setOnDeleteClickListener { recipe ->
            favoriteManager.removeFavorite(recipe) // hapus dari data favorit
            val updatedList = favoriteManager.getFavorites()
            adapter.setData(updatedList) // refresh RecyclerView

            // 🔹 Tampilkan teks & emote jika kosong
            val isEmpty = updatedList.isEmpty()
            binding.emptyTextView.isVisible = isEmpty
            binding.emptyImageView.isVisible = isEmpty
            binding.favoriteRecyclerView.isVisible = !isEmpty

            Toast.makeText(requireContext(), "Resep dihapus dari favorit", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val favorites = favoriteManager.getFavorites()
        adapter.setData(favorites)

        // 🔹 Cek apakah kosong → tampilkan emote & teks
        val isEmpty = favorites.isEmpty()
        binding.emptyTextView.isVisible = isEmpty
        binding.emptyImageView.isVisible = isEmpty
        binding.favoriteRecyclerView.isVisible = !isEmpty
    }
}
