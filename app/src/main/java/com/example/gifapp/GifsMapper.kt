package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem

interface GifsMapper {
    fun mapToDataBaseItem(item: GifNetItem): GifDBEntity
    fun mapToUiItem(item: GifNetItem): GifUiItem
    fun mapToUiItem(item: GifDBEntity): GifUiItem
}
