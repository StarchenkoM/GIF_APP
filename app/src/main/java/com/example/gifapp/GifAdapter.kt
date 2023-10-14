package com.example.gifapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.gifapp.databinding.ItemGifBinding
import com.example.gifapp.ui.home.GifUiItem

class GifAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClicked: ((String) -> Unit)? = null
    var onLoadNext: (() -> Unit)? = null
    var onLoadingResult: (() -> Unit)? = null
    private var gifs: List<GifUiItem> = mutableListOf()

    private val imageLoadListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingResult?.invoke()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingResult?.invoke()
            return false
        }
    }

    fun setData(gifs: List<GifUiItem>) {
        Log.i("mytag", "ADAPTER setData: gifs = ${gifs}")

        this.gifs = gifs
//        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifssViewHolder {
        val binding = ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifssViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gifItem = gifs[position]
        (holder as GifssViewHolder).bind(gifItem)
//        Log.i("mytag*****", "ADAPTER onBindViewHolder: position = $position / ${gifs.lastIndex} CONDITION = ${position == gifs.lastIndex}")

//        if (position == gifs.lastIndex){
//            onLoadNext?.invoke()
//        }
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
            Log.i("mytag", "ADAPTER bind: gifItem.gifLink = ${gifItem.link}")
            with(binding) {
                gifTitle.text = gifItem.title
                Glide.with(root)
                    .load(gifItem.link)
                    .listener(imageLoadListener)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(requestOptions)
                    .into(gifImage)
            }

            binding.itemLayout.setOnClickListener {
                onItemClicked?.invoke(gifItem.link)
            }
        }


    }

}
