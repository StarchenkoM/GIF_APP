package com.example.gifapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gifapp.GifAdapter
import com.example.gifapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding by Delegates.notNull<FragmentHomeBinding>()
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = GifAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiComponents()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerGif.adapter = null
    }


    private fun setupUiComponents() {
        binding.recyclerGif.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerGif.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState ->
                    binding.loaderGroup.isVisible = uiState.isLoading
                    adapter.setData(uiState.gifs)
                }.launchIn(this)
            }
        }
    }

}