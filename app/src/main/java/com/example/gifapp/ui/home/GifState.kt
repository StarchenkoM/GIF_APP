package com.example.gifapp.ui.home

data class GifState(
    val gifs: List<GifUiItem> = listOf(),
    val isLoading: Boolean = false,
    val currentGif: GifUiItem = GifUiItem(),
    val emptyGifWarningEvent: Unit? = null,
)
