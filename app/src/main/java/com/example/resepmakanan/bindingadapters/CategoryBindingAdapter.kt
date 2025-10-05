package com.example.resepmakanan.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.resepmakanan.R

object CategoryBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadCategoryImage(imageView: ImageView, imageUrl: String?) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_chef)
                    .error(R.drawable.ic_chef)
                    .centerInside()
            )
            .into(imageView)
    }
}
