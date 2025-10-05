package com.example.resepmakanan.util

import androidx.recyclerview.widget.DiffUtil
import com.example.resepmakanan.models.Result

class RecipesDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Bandingin pake ID, biar lebih konsisten
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Karena Result adalah data class, == otomatis compare semua field
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
