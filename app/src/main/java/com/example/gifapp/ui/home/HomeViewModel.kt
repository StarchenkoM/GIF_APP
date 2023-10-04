package com.example.gifapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.NetworkConnectivityObserver
import com.example.gifapp.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    init {
//                 TODO: JUST ADD RELOAD BUTTON in case if connection was restored
        loadGifs()
    }

    fun loadGifs(query: String = "") {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val isNetworkConnected = connectivityObserver.observe().first() == Status.Available
            if (isNetworkConnected) {
                delay(1500)
                getGifsUseCase.getGifs(query).onEach { gifs ->
                    _uiState.update { it.copy(gifs = gifs) }
                }.launchIn(this)
            } else {
                _uiState.update { it.copy(isLoading = false, connectionLostEvent = Unit) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun openGif(gifId: String) {
        viewModelScope.launch {
            if (connectivityObserver.observe().first() == Status.Available) {
                _uiState.update { it.copy(selectedGifId = gifId, navigateToGifDetailsEvent = Unit) }
            } else {
                _uiState.update { it.copy(cannotOpenGifEvent = Unit) }
            }
        }
    }

    fun consumeNavigateToGifDetailsEvent() {
        _uiState.update {
            it.copy(navigateToGifDetailsEvent = null)
        }
    }

    fun consumeConnectionLostEvent() {
        _uiState.update { it.copy(connectionLostEvent = null) }
    }

    fun consumeCannotOpenGifEvent() {
        _uiState.update { it.copy(cannotOpenGifEvent = null) }
    }

    // TODO: add consume fun's
}