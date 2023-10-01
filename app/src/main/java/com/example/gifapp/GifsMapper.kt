package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem

interface GifsMapper {
    fun convertNetToDataBaseItem(item: GifNetItem): GifDBItem
    fun convertNetToUiItem(item: GifNetItem): GifUiItem
    fun convertDataBaseItemToUiItem(item: GifDBItem): GifUiItem
}
