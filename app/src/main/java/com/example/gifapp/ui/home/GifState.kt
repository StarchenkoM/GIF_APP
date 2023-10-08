package com.example.gifapp.ui.home

data class GifState(
    val gifs: List<GifUiItem> = listOf(),
    val isLoading: Boolean = false,
    val isNetworkConnected: Boolean = false,
    val selectedGifId: String = "",
    val emptyGifsEvent: Unit? = null,
    val gifsLoadingErrorEvent: Unit? = null,
    val navigateToGifDetailsEvent: Unit? = null,
    val cannotOpenGifEvent: Unit? = null,
)
