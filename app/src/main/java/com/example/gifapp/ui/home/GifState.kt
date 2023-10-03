package com.example.gifapp.ui.home

data class GifState(
    val gifs: List<GifUiItem> = listOf(),
    val isLoading: Boolean = false,
    val currentGif: GifUiItem = GifUiItem(),
    val selectedGifId: String = "",
    val emptyGifWarningEvent: Unit? = null,
    val navigateToGifDetailsEvent: Unit? = null,
    val cannotOpenGifEvent: Unit? = null,
    val connectionLostEvent: Unit? = null,
)
