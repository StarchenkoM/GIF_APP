package com.example.gifapp.ui.home

data class GifState(
    val gifs: List<GifUiItem> = listOf(),
    val isLoading: Boolean = false,
    val isNetworkConnected: Boolean = false,
    val selectedGifId: String = "",
    val cannotOpenGifEvent: Unit? = null,
    val emptyGifsEvent: Unit? = null,
    val gifsLoadingErrorEvent: Unit? = null,
    val navigateToGifDetailsEvent: Unit? = null,
) {
    val isCannotOpenGifEvent: Boolean
        get() = cannotOpenGifEvent != null

    val isEmptyGifsEvent: Boolean
        get() = emptyGifsEvent != null


    val isGifsLoadingErrorEvent: Boolean
        get() = gifsLoadingErrorEvent != null

    val isNavigateToGifDetailsEvent: Boolean
        get() = navigateToGifDetailsEvent != null

    val isLoadingErrorGone: Boolean
        get()= (emptyGifsEvent == null && gifsLoadingErrorEvent == null)
}
