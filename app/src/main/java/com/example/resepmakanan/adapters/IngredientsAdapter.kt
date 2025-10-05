package com.example.resepmakanan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resepmakanan.databinding.ItemIngredientBinding
import com.example.resepmakanan.models.ExtendedIngredient

class IngredientsAdapter(private val ingredients: List<ExtendedIngredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: ExtendedIngredient) {
            binding.ingredientName.text = ingredient.original
            Glide.with(binding.root.context) // pakai context biar lebih aman
                .load("https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}")
                .into(binding.ingredientImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int = ingredients.size
}
