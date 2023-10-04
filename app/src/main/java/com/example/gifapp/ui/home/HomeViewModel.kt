package com.example.gifapp.ui.home

import android.util.Log
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
        _uiState.update { it.copy(gifs = gifs) }
    }

    private val connectivityFlow = connectivityObserver.observe()
//        .map { it == Status.Available }
        .onEach { isNetworkConnected ->
            Log.i(
                "mytag**",
                "VM: connectivityFlow isNetworkConnected = ${isNetworkConnected == Status.Available}"
            )
            if (isNetworkConnected == Status.Available) {
                loadGifs()
                _uiState.update { it.copy(isNetworkConnected = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, connectionLostEvent = Unit) }
            }

        }


    init {
//                 TODO: JUST ADD RELOAD BUTTON in case if connection was restored
        gifFlow.launchIn(viewModelScope)
        connectivityFlow.launchIn(viewModelScope)
//        loadGifs()
    }

    fun loadGifs(query: String = "") {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)
            getGifsUseCase.getGifs(query)

            _uiState.update { it.copy(isLoading = false) }
            delay(5000)
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