package com.example.gifapp.ui

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
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
import com.example.gifapp.R
import com.example.gifapp.databinding.FragmentGifsBinding
import com.example.gifapp.domain.entities.GifUiItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class GifsFragment : Fragment(R.layout.fragment_gifs) {

    private var binding by Delegates.notNull<FragmentGifsBinding>()
    private val viewModel by viewModels<GifsViewModel>()
    private val adapter = GifAdapter()
    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGifsBinding.inflate(inflater, container, false)
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

    override fun onDetach() {
        super.onDetach()
        dialog?.dismiss()
    }

    private fun setupUiComponents() {
        initAdapter()
        initListeners()
    }

    private fun initAdapter() {
        binding.recyclerGif.layoutManager = GridLayoutManager(requireContext(), getGridSpanCount())
        binding.recyclerGif.adapter = adapter
        adapter.onItemClicked = { gifItem -> viewModel.openGif(gifItem) }
    }

    private fun getGridSpanCount(): Int {
        val orientation = resources.configuration.orientation
        return if (orientation == ORIENTATION_LANDSCAPE) 3 else 2
    }

    private fun initListeners() {
        binding.manageGifsBtn.setOnClickListener {
            viewModel.handleButtonClick()
        }
        binding.optionsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.updateDeleteOptionPosition(isChecked)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState ->
                    setViewsVisibility(uiState)
                    adapter.setData(uiState.gifs)
                    setButtonText(uiState.isDeleteOptionEnabled)
                    when {
                        uiState.isCannotOpenGifEvent -> displayWarningDialog()
                        uiState.isEmptyGifsEvent -> showSnackbar(R.string.no_gifs_message)
                        uiState.isGifsLoadingErrorEvent -> showSnackbar(R.string.gif_loading_error_message)
                        uiState.isNavigateToGifDetailsEvent -> openGifDetails(uiState.selectedGif)
                    }
                }.launchIn(this)
            }
        }
    }

    private fun setViewsVisibility(uiState: GifState) {
        with(binding) {
            loaderGroup.isVisible = uiState.isLoading
            connectionLostWarning.isGone = uiState.isNetworkAvailable
            gifsLoadingErrorText.isGone = uiState.isEmptyListMessageDisplayed
            manageGifsBtn.isVisible = uiState.gifs.isNotEmpty()
            manageGifsBtn.isEnabled = !uiState.isLoading
            optionsSwitch.isVisible = uiState.gifs.isNotEmpty()
        }
    }

    private fun setButtonText(isDeleteOptionEnabled: Boolean) {
        val buttonText = if (isDeleteOptionEnabled) {
            R.string.options_switch_text_delete
        } else {
            R.string.load_next_btn_text
        }
        binding.manageGifsBtn.setText(buttonText)
    }

    private fun displayWarningDialog() {
        showWarningDialog()
        viewModel.consumeCannotOpenGifEvent()
    }

    private fun showWarningDialog() {
        dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(R.string.warning_dialog_title)
            .setMessage(R.string.warning_dialog_message)
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton(R.string.positive_btn_text, null)
            .create()
        dialog?.show()
    }

    private fun showSnackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, duration).show()
        viewModel.consumeLoadingErrorEvent()
    }

    private fun openGifDetails(selectedGif: GifUiItem) {
        navigateToGifDetails(selectedGif.title, selectedGif.link)
        viewModel.consumeNavigateToGifDetailsEvent()
    }

    private fun navigateToGifDetails(gifTitle: String, gifLink: String) {
        val direction = GifsFragmentDirections.actionGifsFragmentToGifDetain(gifTitle, gifLink)
        findNavController().navigate(direction)
    }

}