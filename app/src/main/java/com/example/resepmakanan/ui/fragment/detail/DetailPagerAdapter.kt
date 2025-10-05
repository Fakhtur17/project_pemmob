package com.example.resepmakanan.ui.fragment.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.resepmakanan.models.Result

class DetailPagerAdapter(
    activity: AppCompatActivity,
    private val result: Result
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment.newInstance(result)
            1 -> IngredientsFragment.newInstance(result.extendedIngredients ?: emptyList())
            else -> InstructionsFragment.newInstance(result.instructions ?: "")
        }
    }
}
