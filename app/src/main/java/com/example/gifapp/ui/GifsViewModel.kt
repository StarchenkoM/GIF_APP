package com.example.gifapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.data.repository.GifsFetchingResponse.EmptyResponseError
import com.example.gifapp.data.repository.GifsFetchingResponse.LoadingError
import com.example.gifapp.domain.entities.GifUiItem
import com.example.gifapp.domain.usecases.DeleteGifsUseCase
import com.example.gifapp.domain.usecases.GetGifsUseCase
import com.example.gifapp.util.NetworkConnectivityObserver
import com.example.gifapp.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val OFFSET_INCREMENT = 25

@HiltViewModel
class GifsViewModel @Inject constructor(
    private val getGifsUseCase: GetGifsUseCase,
    private val deleteGifsUseCase: DeleteGifsUseCase,
    connectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GifState())
    val uiState: StateFlow<GifState> = _uiState.asStateFlow()

    private var offset = 0

    private val gifFlow = getGifsUseCase.gifFlow.onEach { gifs ->
        offset = gifs.size
        _uiState.update { it.copy(gifs = gifs) }
        if (gifs.isEmpty()) {
            loadGifs()
        }
    }

    private val connectivityFlow = connectivityObserver.observe()
        .onEach { connectionStatus ->
            if (isLoadingRequired(connectionStatus)) {
                loadGifs()
            }
            _uiState.update { it.copy(isNetworkConnected = connectionStatus == Status.Available) }
        }

    init {
        gifFlow.launchIn(viewModelScope)
        connectivityFlow.launchIn(viewModelScope)
    }

    fun openGif(gifItem: GifUiItem) {
        viewModelScope.launch {
            if (_uiState.value.isNetworkConnected == true) {
                _uiState.update { it.copy(selectedGif = gifItem, navigateToGifDetailsEvent = Unit) }
            } else {
                _uiState.update { it.copy(cannotOpenGifEvent = Unit) }
            }
        }
    }

    fun updateDeleteOptionPosition(isChecked: Boolean) {
        _uiState.update { it.copy(isDeleteOptionEnabled = isChecked) }
    }

    fun handleButtonClick() {
        if (_uiState.value.isDeleteOptionEnabled) {
            deleteAllGifs()
        } else {
            loadNext()
        }
    }

    private fun isLoadingRequired(connectionStatus: Status): Boolean {
        val isNetworkAvailable = connectionStatus == Status.Available
        val wasConnectionLost = _uiState.value.isNetworkConnected?.let { !it } ?: false
        val isGifsListEmpty = _uiState.value.gifs.isEmpty()
        return isNetworkAvailable && wasConnectionLost && isGifsListEmpty
    }

    private fun loadGifs(offset: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true, emptyGifsEvent = null, gifsLoadingErrorEvent = null)
            }
            when (getGifsUseCase.getGifs(offset)) {
                is EmptyResponseError -> _uiState.update { it.copy(emptyGifsEvent = Unit) }
                is LoadingError -> _uiState.update { it.copy(gifsLoadingErrorEvent = Unit) }
                else -> {}
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun deleteAllGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteGifsUseCase.deleteGifs()
        }
    }

    private fun loadNext() {
        offset += OFFSET_INCREMENT
        loadGifs(offset)
    }

    fun consumeNavigateToGifDetailsEvent() {
        _uiState.update { it.copy(navigateToGifDetailsEvent = null) }
    }

    fun consumeCannotOpenGifEvent() {
        _uiState.update { it.copy(cannotOpenGifEvent = null) }
    }

    fun consumeLoadingErrorEvent() {
        _uiState.update { it.copy(emptyGifsEvent = null, gifsLoadingErrorEvent = null) }
    }

}