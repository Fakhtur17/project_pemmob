package com.example.resepmakanan.ui.fragment.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.resepmakanan.R
import com.todkars.shimmer.ShimmerRecyclerView

class RecipesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)

        val recyclerView = view.findViewById<ShimmerRecyclerView>(R.id.recyclerView)

        // Tampilkan shimmer saat data belum ada
        recyclerView.showShimmer()

        // Nanti setelah dapat data dari API/Database, shimmer disembunyikan:
        // recyclerView.hideShimmer()
        // recyclerView.adapter = RecipesAdapter(data)

        return view
    }
}