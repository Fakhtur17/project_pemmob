package com.example.resepmakanan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.resepmakanan.R
import com.example.resepmakanan.models.Result

class LatestRecipeAdapter(
    private val recipes: List<Result>,
    private val onItemClick: (Result) -> Unit
) : RecyclerView.Adapter<LatestRecipeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRecipe: ImageView = view.findViewById(R.id.imgRecipe)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_latest_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.tvTitle.text = recipe.title
        holder.tvSummary.text = recipe.summary?.replace(Regex("<.*?>"), "") ?: "No description"
        holder.tvTime.text = "⏱ ${recipe.readyInMinutes ?: "?"} min"

        holder.imgRecipe.load(recipe.image) {
            crossfade(true)
            placeholder(R.drawable.ic_chef)
            error(R.drawable.ic_chef)
        }

        holder.itemView.setOnClickListener { onItemClick(recipe) }
    }

    override fun getItemCount() = recipes.size
}
