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
import com.example.resepmakanan.databinding.FragmentOverviewBinding
import com.example.resepmakanan.models.Result

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding
    private lateinit var recipe: Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = requireArguments().getParcelable("recipe")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)

        Glide.with(requireContext()).load(recipe.image).into(binding.recipeImageView)
        binding.titleTextView.text = recipe.title
        binding.likesTextView.text = recipe.aggregateLikes.toString()
        binding.minutesTextView.text = "${recipe.readyInMinutes} minutes"

        binding.summaryTextView.text = HtmlCompat.fromHtml(
            recipe.summary,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        // badges
        binding.vegetarianIcon.isVisible = recipe.vegetarian
        binding.glutenFreeIcon.isVisible = recipe.glutenFree
        binding.veganIcon.isVisible = recipe.vegan
        binding.cheapIcon.isVisible = recipe.cheap
        binding.dairyFreeIcon.isVisible = recipe.dairyFree
        binding.healthyIcon.isVisible = recipe.veryHealthy

        return binding.root
    }

    companion object {
        fun newInstance(recipe: Result): OverviewFragment {
            return OverviewFragment().apply {
                arguments = bundleOf("recipe" to recipe)
            }
        }
    }
}