package com.example.gifapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gifapp.R
import com.example.gifapp.databinding.ItemGifBinding
import com.example.gifapp.domain.entities.GifUiItem

private const val CORNERS_RADIUS = 22

class GifAdapter : RecyclerView.Adapter<GifAdapter.GifsViewHolder>() {
    var onItemClicked: ((GifUiItem) -> Unit)? = null
    private var gifs: List<GifUiItem> = mutableListOf()

    fun setData(gifs: List<GifUiItem>) {
        this.gifs = gifs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifsViewHolder {
        val binding = ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifsViewHolder, position: Int) {
        val gifItem = gifs[position]
        holder.bind(gifItem)
    }

    override fun getItemCount(): Int = gifs.size

    inner class GifsViewHolder(
        private val binding: ItemGifBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gifItem: GifUiItem) {
            with(binding) {
                gifTitle.text = gifItem.title
                Glide.with(root)
                    .load(gifItem.link)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(CenterCrop(), RoundedCorners(CORNERS_RADIUS))
                    .placeholder(R.drawable.ic_gif)
                    .into(gifImage)
            }

            binding.itemLayout.setOnClickListener {
                onItemClicked?.invoke(gifItem)
            }
        }
    }

}
