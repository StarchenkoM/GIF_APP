package com.example.gifapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
        initSearch()

        // TODO: remove
        binding.deleteGifs.setOnClickListener {
            viewModel.deleteAllGifs()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState ->

                    Log.i("mytag**", "setupObservers: emptyGifsEvent = ${uiState.emptyGifsEvent}")
                    Log.i("mytag**", "setupObservers: gifsLoadingErrorEvent = ${uiState.gifsLoadingErrorEvent}")
                    Log.i("mytag**", "setupObservers: emptyGifsEvent || gifsLoadingErrorEvent = ${uiState.emptyGifsEvent != null || uiState.gifsLoadingErrorEvent != null}")
                    binding.loaderGroup.isVisible = uiState.isLoading
                    binding.searchHome.isGone = !uiState.isNetworkConnected
                    binding.connectionLostWarning.isGone = uiState.isNetworkConnected
                    binding.gifsLoadingErrorText.isGone = !(uiState.emptyGifsEvent != null || uiState.gifsLoadingErrorEvent != null)
                    binding.recyclerGif.isVisible = (uiState.emptyGifsEvent == null && uiState.gifsLoadingErrorEvent == null)
                    adapter.setData(uiState.gifs)

                    if (uiState.cannotOpenGifEvent != null) {
                        showWarningDialog()
                        viewModel.consumeCannotOpenGifEvent()
                    }

                    if (uiState.emptyGifsEvent != null) {
                        displayMessage("No gifs by this request")
                    }

                    if (uiState.gifsLoadingErrorEvent != null) {
                        displayMessage("Gifs loading error :( \n Please try later")
                    }

                    if (uiState.navigateToGifDetailsEvent != null) {
                        val action =
                            HomeFragmentDirections.actionGifsFragmentToGifDetain(uiState.selectedGifId)
                        findNavController().navigate(action)
                        viewModel.consumeNavigateToGifDetailsEvent()
                    }

                }.launchIn(this)
            }
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

    private fun displayMessage(message: String) {
        with(binding) {
            gifsLoadingErrorText.text = message
        }
    }

    private fun initAdapter() {
        binding.recyclerGif.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerGif.adapter = adapter
        adapter.onItemClicked = { gifId -> viewModel.openGif(gifId) }
    }

    //TODO remove?
    private fun initSearch() {
        val searchView = binding.searchHome
        setSearchElementColors(searchView)
        setOnQueryTextListener(searchView)
    }

    private fun setSearchElementColors(searchView: SearchView) {
        val elementsColor = ContextCompat.getColor(requireContext(), R.color.text2)

        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        val closeIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        searchEditText.setTextColor(elementsColor)
        searchEditText.setHintTextColor(elementsColor)
        searchIcon.setColorFilter(elementsColor)
        closeIcon.setColorFilter(elementsColor)
    }

    private fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.loadGifs(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

}