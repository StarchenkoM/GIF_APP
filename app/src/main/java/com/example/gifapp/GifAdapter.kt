package com.example.gifapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.gifapp.databinding.ItemGifBinding
import com.example.gifapp.ui.home.GifUiItem

class GifAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var gifs: List<GifUiItem> = mutableListOf()

    fun setData(gifs: List<GifUiItem>) {
        Log.i("mytag", "ADAPTER setData: gifs = ${gifs}")

        this.gifs = gifs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifssViewHolder {
        val binding = ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifssViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gifItem = gifs[position]
        (holder as GifssViewHolder).bind(gifItem)
    }

    override fun getItemCount(): Int = gifs.size

    inner class GifssViewHolder(
        private val binding: ItemGifBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gifItem: GifUiItem) {
            val requestOptions = RequestOptions().apply {
//                transform(CenterCrop(), RoundedCorners(12))
                transform(RoundedCorners(12))
            }
            Log.i("mytag", "ADAPTER bind: gifItem.gifLink = ${gifItem.gifLink}")
            with(binding) {
                gifTitle.text = gifItem.title
                Glide.with(binding.root)
                    .load(gifItem.gifLink)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .apply(requestOptions)
                    .into(binding.gifImage)
            }
        }


    }

}
