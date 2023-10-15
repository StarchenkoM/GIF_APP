package com.example.gifapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gifapp.GifAdapter
import com.example.gifapp.R
import com.example.gifapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


private const val GRID_SPAN_COUNT = 2

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var binding by Delegates.notNull<FragmentHomeBinding>()
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = GifAdapter()
    private var dialog: AlertDialog? = null

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
        initAdapter()
        // TODO: remove
        binding.deleteGifs.setOnClickListener {
            viewModel.deleteAllGifs()
        }
        binding.loadNextGifs.setOnClickListener {
            viewModel.loadNext()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState ->

                    Log.i("mytag**", "setupObservers: emptyGifsEvent = ${uiState.emptyGifsEvent}")

                    setViewsVisibility(uiState)
                    adapter.setData(uiState.gifs)
                    when {
                        uiState.isCannotOpenGifEvent -> displayWarningDialog()
                        uiState.isEmptyGifsEvent -> showSnackbar("No gifs by this request")
                        uiState.isGifsLoadingErrorEvent -> showSnackbar("Gifs loading error :(\nPlease check your network connection")
                        uiState.isNavigateToGifDetailsEvent -> openGifDetails(uiState.selectedGif)
                    }
                }.launchIn(this)
            }
        }
    }

    private fun openGifDetails(selectedGif: GifUiItem) {
        navigateToGifDetails(selectedGif.title, selectedGif.link)
        viewModel.consumeNavigateToGifDetailsEvent()
    }

    private fun navigateToGifDetails(gifTitle: String, gifLink: String) {
        val direction = HomeFragmentDirections.actionGifsFragmentToGifDetain(gifTitle, gifLink)
        findNavController().navigate(direction)
    }

    private fun displayWarningDialog() {
        showWarningDialog()
        viewModel.consumeCannotOpenGifEvent()
    }

    private fun setViewsVisibility(uiState: GifState) {
        with(binding) {
            loaderGroup.isVisible = uiState.isLoading
            connectionLostWarning.isGone = uiState.isNetworkAvailable
            gifsLoadingErrorText.isGone = uiState.isEmptyListMessageDisplayed
            loadNextGifs.isVisible = uiState.gifs.isNotEmpty()
            loadNextGifs.isEnabled = !uiState.isLoading
        }
    }

    private fun showWarningDialog() {
        dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle("Warning")
            .setMessage("Gif cannot be open. Please check your network connection")
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton("OK", null)
            .create()
        dialog?.show()
    }

    override fun onDetach() {
        super.onDetach()
        dialog?.dismiss()
    }

    private fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, duration).show()
        viewModel.consumeLoadingErrorEvent()
    }

    private fun initAdapter() {
        binding.recyclerGif.layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        binding.recyclerGif.adapter = adapter
        adapter.onItemClicked = { gifItem -> viewModel.openGif(gifItem) }
    }

}