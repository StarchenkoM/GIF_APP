package com.example.gifapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifapp.GetGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(GifState())
    val uiState: StateFlow<GifState> = _uiState.asStateFlow()

    init {
        loadGifs()
    }

    private fun loadGifs() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            delay(2500)
        }
        getGifsUseCase.getGifs().onEach { gifs ->
            _uiState.update { it.copy(gifs = gifs) }
        }.launchIn(viewModelScope)
        _uiState.update { it.copy(isLoading = false) }
    }
}