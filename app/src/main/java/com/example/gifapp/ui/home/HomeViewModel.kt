package com.example.gifapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.NetworkConnectivityObserver
import com.example.gifapp.Status
import dagger.hilt.android.lifecycle.HiltViewModel
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
        loadGifs()
    }


    fun loadGifs(query: String = "") {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)

            val testList = getGifsUseCase.getGifs(query)
            Log.i("mytag", "VM getGifs: testList.first = ${testList.first().map { it.title }}")

            testList.onEach { gifs ->
                Log.i("mytag", "VM getGifs:onEach() gifs = ${gifs.map { it.title }}")
                _uiState.update { it.copy(gifs = gifs) }
            }.launchIn(this)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun openGif(gifId: String) {
        viewModelScope.launch {
            val isNetworkConnected = connectivityObserver.observe().first() == Status.Available
            if (isNetworkConnected) {
                _uiState.update {
                    it.copy(
                        selectedGifId = gifId,
                        navigateToGifDetailsEvent = Unit
                    )
                }
            } else {
                _uiState.update { it.copy(gifUnavailableEvent = Unit) }
            }
        }
    }

    fun consumeNavigateToGifDetailsEvent() {
        _uiState.update {
            it.copy(navigateToGifDetailsEvent = null)
        }
    }

    // TODO: add consume fun's
}