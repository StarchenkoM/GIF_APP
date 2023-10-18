package com.example.gifapp.ui

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gifapp.R
import com.example.gifapp.databinding.FragmentGifDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class GifDetailsFragment : Fragment() {

    private var binding by Delegates.notNull<FragmentGifDetailsBinding>()
    private val args: GifDetailsFragmentArgs by navArgs()

    private val imageLoadListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            displayErrorMessage()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            binding.loaderGroup.isGone = true
            return false
        }
    }

    private val shareGifChooser =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGifDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGif()
        shareGifLink()
    }

    private fun showGif() {
        setGifTitle()
        if (args.link.isNotEmpty()) {
            setGifImage()
        } else {
            displayErrorMessage()
        }
        binding.shareGif.isEnabled = args.link.isNotEmpty()
    }

    private fun setGifTitle() {
        if (args.title.isNotEmpty()) {
            binding.gifTitle.text = args.title
        } else {
            binding.gifTitle.isVisible = false
        }
    }

    private fun setGifImage() {
        Glide.with(requireContext())
            .load(args.link)
            .listener(imageLoadListener)
            .transform(FitCenter(), RoundedCorners(42))
            .placeholder(R.drawable.ic_gif)
            .into(binding.gifDetailImage)
    }

    private fun shareGifLink() {
        binding.shareGif.setOnClickListener {
            args.link.ifEmpty {
                showSnackbar(R.string.gif_could_not_be_shared)
                return@setOnClickListener
            }
            openChooser()
        }
    }

    private fun openChooser() {
        val chooserIntent = Intent(ACTION_SEND).apply {
            putExtra(EXTRA_TEXT, args.link)
            type = "text/plain"
            createChooser(this, getString(R.string.share_gif))
        }
        shareGifChooser.launch(chooserIntent)
    }

    private fun showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, duration).show()
    }

    private fun displayErrorMessage() {
        with(binding) {
            loaderGroup.isGone = true
            gifLoadingErrorText.isVisible = true
            gifLoadingErrorText.text = getString(R.string.gif_loading_error_message)
        }
    }
}