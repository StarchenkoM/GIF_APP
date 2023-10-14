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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.gifapp.GifAdapter
import com.example.gifapp.R
import com.example.gifapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


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
        // TODO: remove
        binding.loadNextGifs.setOnClickListener {
            viewModel.loadNext()
        }
        initScrollListener()
    }


        private fun initScrollListener() {
        binding.recyclerGif.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val condition = position == (adapter.itemCount - 1)
//                Log.i("mytag*****", "FRAGMENT onScrolled: position = $position / ${adapter.itemCount-1} CONDITION = $condition")

                if (condition) {
                    viewModel.loadNext()
                }
            }
        })
    }
//    private fun initScrollListener() {
//        binding.recyclerGif.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
//                val visibleItemCount: Int = linearLayoutManager.childCount
//                val totalItemCount: Int = linearLayoutManager.itemCount
//                val firstVisibleItemPosition: Int =
//                    linearLayoutManager.findFirstVisibleItemPosition()
//                val condition = visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0
//                if (condition) {
//                    // The RecyclerView has been scrolled to the bottom, load more data here
//                    // Call your data loading method here
//                    // Example: fetchData();
//                    viewModel.loadNext()
//                }
//            }
//        })
//    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState ->

                    Log.i("mytag**", "setupObservers: emptyGifsEvent = ${uiState.emptyGifsEvent}")

                    setViewsVisibility(uiState)
                    adapter.setData(uiState.gifs)
                    if (!binding.recyclerGif.isComputingLayout && binding.recyclerGif.scrollState == SCROLL_STATE_IDLE) {
                        binding.recyclerGif.adapter?.notifyDataSetChanged()
                    }

                    when {
                        uiState.isCannotOpenGifEvent -> displayWarningDialog()
                        uiState.isEmptyGifsEvent -> displaySnackbar("No gifs by this request")
                        uiState.isGifsLoadingErrorEvent -> displaySnackbar("Gifs loading error :(\nPlease check your network connection")
                        uiState.isNavigateToGifDetailsEvent -> manageNavigationToGifDetails(uiState.selectedGifId)
                    }
                }.launchIn(this)
            }
        }
    }

    private fun manageNavigationToGifDetails(gifId: String) {
        navigateToGifDetails(gifId)
        viewModel.consumeNavigateToGifDetailsEvent()
    }

    private fun navigateToGifDetails(gifId: String) {
        val direction = HomeFragmentDirections.actionGifsFragmentToGifDetain(gifId)
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

    private fun displaySnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        viewModel.consumeLoadingErrorEvent()
    }

    private fun initAdapter() {
        binding.recyclerGif.layoutManager =
            GridLayoutManager(requireContext(), 2)// TODO: move to constant
        binding.recyclerGif.adapter = adapter
        adapter.onItemClicked = { gifId -> viewModel.openGif(gifId) }
        adapter.onLoadNext = { viewModel.loadNext() }
        adapter.onLoadingResult = { viewModel.consumeLoading() }

    }

}