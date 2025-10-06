package com.example.resepmakanan.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.resepmakanan.R
import com.example.resepmakanan.databinding.FragmentOverviewBinding
import com.example.resepmakanan.models.Result
import com.example.resepmakanan.util.FavoriteManager

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding
    private lateinit var recipe: Result
    private lateinit var favoriteManager: FavoriteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = requireArguments().getParcelable("recipe")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        favoriteManager = FavoriteManager(requireContext())

        // 🔹 Tampilkan data resep
        Glide.with(requireContext()).load(recipe.image).into(binding.recipeImageView)
        binding.titleTextView.text = recipe.title
        binding.likesTextView.text = recipe.aggregateLikes.toString()
        binding.minutesTextView.text = "${recipe.readyInMinutes} minutes"
        binding.summaryTextView.text = HtmlCompat.fromHtml(
            recipe.summary,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        // 🔹 Tampilkan badge kondisi
        binding.vegetarianIcon.isVisible = recipe.vegetarian
        binding.glutenFreeIcon.isVisible = recipe.glutenFree
        binding.veganIcon.isVisible = recipe.vegan
        binding.cheapIcon.isVisible = recipe.cheap
        binding.dairyFreeIcon.isVisible = recipe.dairyFree
        binding.healthyIcon.isVisible = recipe.veryHealthy

        // 🔹 Atur status awal ikon favorit
        updateFavoriteIcon()

        // 🔹 Tombol klik favorite
        binding.btnLike.setOnClickListener {
            if (favoriteManager.isFavorite(recipe)) {
                favoriteManager.removeFavorite(recipe)
            } else {
                favoriteManager.addFavorite(recipe)
            }
            updateFavoriteIcon()
        }

        return binding.root
    }

    private fun updateFavoriteIcon() {
        if (favoriteManager.isFavorite(recipe)) {
            binding.btnLike.setImageResource(R.drawable.ic_star_filled)
        } else {
            binding.btnLike.setImageResource(R.drawable.ic_star)
        }
    }

    companion object {
        fun newInstance(recipe: Result): OverviewFragment {
            return OverviewFragment().apply {
                arguments = bundleOf("recipe" to recipe)
            }
        }
    }
}
