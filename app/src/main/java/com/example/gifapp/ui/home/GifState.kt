package com.example.gifapp.ui.home

data class GifState(
    val gifs: List<GifUiItem> = listOf(),
    val isLoading: Boolean = false,
    val isNetworkConnected: Boolean? = null,
    val selectedGifId: String = "",
    val cannotOpenGifEvent: Unit? = null,
    val emptyGifsEvent: Unit? = null,
    val gifsLoadingErrorEvent: Unit? = null,
    val navigateToGifDetailsEvent: Unit? = null,
) {

    val isEmptyListMessageDisplayed: Boolean
        get() = gifs.isNotEmpty()

    val isNetworkAvailable: Boolean
        get() = (isNetworkConnected != null && isNetworkConnected)

    val isCannotOpenGifEvent: Boolean
        get() = cannotOpenGifEvent != null

    val isEmptyGifsEvent: Boolean
        get() = emptyGifsEvent != null

    val isGifsLoadingErrorEvent: Boolean
        get() = (gifsLoadingErrorEvent != null && !isCannotOpenGifEvent)

    val isNavigateToGifDetailsEvent: Boolean
        get() = navigateToGifDetailsEvent != null

}
