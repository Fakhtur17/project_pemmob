package com.example.resepmakanan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resepmakanan.R
import com.example.resepmakanan.models.Result

class SliderAdapter(
    private val sliderItems: List<Result>,
    private val onItemClick: (Result) -> Unit // ✅ tambahan listener
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sliderImage: ImageView = itemView.findViewById(R.id.sliderImage)
        val sliderTitle: TextView = itemView.findViewById(R.id.sliderTitle)

        fun bind(item: Result) {
            // load gambar
            Glide.with(sliderImage.context)
                .load(item.image)
                .placeholder(R.drawable.ic_placeholder)
                .into(sliderImage)

            sliderTitle.text = item.title

            // klik listener
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItems[position])
    }

    override fun getItemCount(): Int = sliderItems.size
}
