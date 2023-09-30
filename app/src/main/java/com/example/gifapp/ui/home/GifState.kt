package com.example.gifapp.ui.home

data class GifState(
    val gifs: List<GifUiModel> = listOf(),
    val isLoading: Boolean = false,
    val currentGif: GifUiModel = GifUiModel(),
    val emptyGifWarningEvent: Unit? = null,
)
