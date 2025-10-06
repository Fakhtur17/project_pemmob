package com.example.resepmakanan.util

import android.content.Context
import com.example.resepmakanan.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteManager(context: Context) {

    private val prefs = context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getFavorites(): MutableList<Result> {
        val json = prefs.getString("favorites", null)
        val type = object : TypeToken<MutableList<Result>>() {}.type
        return if (json != null) gson.fromJson(json, type) else mutableListOf()
    }

    fun saveFavorites(favorites: MutableList<Result>) {
        prefs.edit().putString("favorites", gson.toJson(favorites)).apply()
    }

    fun addFavorite(recipe: Result) {
        val favorites = getFavorites()
        if (!favorites.any { it.id == recipe.id }) {
            favorites.add(recipe)
            saveFavorites(favorites)
        }
    }

    fun removeFavorite(recipe: Result) {
        val favorites = getFavorites()
        favorites.removeAll { it.id == recipe.id }
        saveFavorites(favorites)
    }

    fun isFavorite(recipe: Result): Boolean {
        return getFavorites().any { it.id == recipe.id }
    }
}
