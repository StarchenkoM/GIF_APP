package com.example.gifapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.NetworkConnectivityObserver
import com.example.gifapp.GifsFetchingResponse.EmptyResponseError
import com.example.gifapp.GifsFetchingResponse.LoadingError
import com.example.gifapp.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGifsUseCase: GetGifsUseCase,
    private val connectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GifState())
    val uiState: StateFlow<GifState> = _uiState.asStateFlow()

    private val gifFlow = getGifsUseCase.gifFlow.onEach { gifs ->
        Log.i("mytag**", "VM: gifFlow gifs.size = ${gifs.size}")
        if (gifs.isEmpty()) {
            loadGifs()
        }
        _uiState.update { it.copy(gifs = gifs) }
    }

    private val connectivityFlow = connectivityObserver.observe()
        .onEach { connectionStatus ->
            if (isNetworkRestored(connectionStatus)) {
                loadGifs()
            }
            _uiState.update { it.copy(isNetworkConnected = connectionStatus == Status.Available) }
        }

    private fun isNetworkRestored(connectionStatus: Status) =
        connectionStatus == Status.Available && !_uiState.value.isNetworkConnected


    init {
        gifFlow.launchIn(viewModelScope)
        connectivityFlow.launchIn(viewModelScope)
    }

    fun loadGifs(query: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true, emptyGifsEvent = null, gifsLoadingErrorEvent = null)
            }
            delay(1500)// TODO: remove
            when (getGifsUseCase.getGifs(query)) {
                is EmptyResponseError -> _uiState.update { it.copy(emptyGifsEvent = Unit) }
                is LoadingError -> _uiState.update { it.copy(gifsLoadingErrorEvent = Unit) }
                else -> {}
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteAllGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            getGifsUseCase.deleteGifs()
        }
    }

    fun openGif(gifId: String) {
        viewModelScope.launch {
            if (_uiState.value.isNetworkConnected) {
                _uiState.update { it.copy(selectedGifId = gifId, navigateToGifDetailsEvent = Unit) }
            } else {
                _uiState.update { it.copy(cannotOpenGifEvent = Unit) }
            }
        }
    }

    fun consumeNavigateToGifDetailsEvent() {
        _uiState.update { it.copy(navigateToGifDetailsEvent = null) }
    }

    fun consumeCannotOpenGifEvent() {
        _uiState.update { it.copy(cannotOpenGifEvent = null) }
    }

    // TODO: add consume fun's
}