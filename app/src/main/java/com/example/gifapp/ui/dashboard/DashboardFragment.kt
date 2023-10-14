package com.example.gifapp.ui.dashboard

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.gifapp.R
import com.example.gifapp.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {


    // TODO: add share button
    private var binding by Delegates.notNull<FragmentDashboardBinding>()
    private val viewModel by viewModels<DashboardViewModel>()
    private val args: DashboardFragmentArgs by navArgs()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGif()
    }

    private fun showGif() {
        if (args.title.isNotEmpty()) {
            binding.gifTitle.text = args.title
        } else {
            binding.gifTitle.isVisible = false
        }


//        val requestOptions = RequestOptions().apply {
//            transform(CenterCrop(), RoundedCorners(22))
//        }
        if (args.link.isNotEmpty()) {
            Glide.with(requireContext())
                .load(args.link)
                .listener(imageLoadListener)
                .transform(CenterCrop(), RoundedCorners(42))
                .placeholder(R.drawable.ic_gif)
//                .apply(requestOptions)
                .into(binding.gifDetailImage)
        } else {
            displayErrorMessage()
        }

    }

    private fun displayErrorMessage() {
        with(binding) {
            loaderGroup.isGone = true
            gifLoadingErrorText.isVisible = true
            gifLoadingErrorText.text = "Gif loading error :( \n Please try later"
        }
    }
}