package com.example.resepmakanan.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resepmakanan.adapters.IngredientsAdapter
import com.example.resepmakanan.databinding.FragmentIngredientsBinding
import com.example.resepmakanan.models.ExtendedIngredient

class IngredientsFragment : Fragment() {

    private lateinit var binding: FragmentIngredientsBinding
    private lateinit var ingredients: List<ExtendedIngredient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingredients = requireArguments().getParcelableArrayList("ingredients")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = IngredientsAdapter(ingredients)
        return binding.root
    }

    companion object {
        fun newInstance(ingredients: List<ExtendedIngredient>): IngredientsFragment {
            return IngredientsFragment().apply {
                arguments = bundleOf("ingredients" to ArrayList(ingredients))
            }
        }
    }
}
