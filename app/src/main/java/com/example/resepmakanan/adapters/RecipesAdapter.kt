package com.example.resepmakanan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.resepmakanan.databinding.RecipesRowLayoutBinding
import com.example.resepmakanan.models.Result
import com.example.resepmakanan.util.RecipesDiffUtil
import android.widget.ImageView
import com.example.resepmakanan.R

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    // 🔹 Tambahan: listener
    private var onItemClickListener: ((Result) -> Unit)? = null

    private var onDeleteClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
    fun setOnDeleteClickListener(listener: (Result) -> Unit) {
        onDeleteClickListener = listener
    }

    class MyViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            // binding variable "result" ke layout
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentRecipe)
        }

        // 🔹 Tangani klik tombol hapus
        holder.itemView.findViewById<ImageView>(R.id.delete_favorite)?.setOnClickListener {
            onDeleteClickListener?.invoke(currentRecipe)
        }

    }

    override fun getItemCount(): Int = recipes.size

    /**
     * Fungsi untuk update data adapter
     */
    fun setData(newData: List<Result>) {
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}
