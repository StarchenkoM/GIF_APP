package com.example.gifapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.GifsFetchingResponse.EmptyResponseError
import com.example.gifapp.GifsFetchingResponse.LoadingError
import com.example.gifapp.NetworkConnectivityObserver
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

    private val offsetFlow = MutableStateFlow(_uiState.value.gifs.size)

    private val gifFlow = getGifsUseCase.gifFlow.onEach { gifs ->
        offsetFlow.value = gifs.size
        Log.i(
            "mytag*****",
            "VM: gifFlow gifs.size = ${gifs.size} offsetFlow.value = ${offsetFlow.value}"
        )
        _uiState.update { it.copy(gifs = gifs) }
        if (gifs.isEmpty()) {
            loadGifs()
        }
    }



    private val connectivityFlow = connectivityObserver.observe()
        .onEach { connectionStatus ->
            if (isNetworkRestored(connectionStatus) && _uiState.value.gifs.isEmpty()) {
                loadGifs()
            }
            _uiState.update { it.copy(isNetworkConnected = connectionStatus == Status.Available) }
        }

    private fun isNetworkRestored(connectionStatus: Status) =
        connectionStatus == Status.Available && !_uiState.value.isNetworkConnected


    init {
        Log.i("mytag*****", "VM: ================================================")
        Log.i("mytag*****", "VM: ================================================")
        Log.i("mytag*****", "VM: ================================================")
        gifFlow.launchIn(viewModelScope)
        connectivityFlow.launchIn(viewModelScope)
    }

    private fun loadGifs(offset: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true, emptyGifsEvent = null, gifsLoadingErrorEvent = null)
            }
            delay(1500)// TODO: remove
            when (getGifsUseCase.getGifs(offset)) {
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


    fun loadNext() {
//        offsetFlow.value +=1 // TODO: move to constant
//        val offset = offsetFlow.value
//        Log.i("mytag*****", "VM: loadNext offset = $offset")
        Log.i("mytag*****", "VM: loadNext offset BEFORE INCREMENT = ${offsetFlow.value}")
        offsetFlow.value += 1 // TODO: move to constant
        Log.i("mytag*****", "VM: loadNext offset AFTER INCREMENT = ${offsetFlow.value}")

//        loadGifs(offset)
        loadGifs(offsetFlow.value)
    }

    fun consumeNavigateToGifDetailsEvent() {
        _uiState.update { it.copy(navigateToGifDetailsEvent = null) }
    }

    fun consumeCannotOpenGifEvent() {
        _uiState.update { it.copy(cannotOpenGifEvent = null) }
    }


    // TODO: add consume fun's
}