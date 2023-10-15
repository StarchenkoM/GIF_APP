package com.example.gifapp.util

import com.example.gifapp.GifDBEntity
import com.example.gifapp.GifNetItem
import com.example.gifapp.ui.home.GifUiItem

fun List<GifNetItem>.mapToDataBaseEntities() = this.map { item ->
    GifDBEntity(
        id = item.id,
        title = item.title,
        link = item.images.imageDetails.url,
    )
}

fun List<GifDBEntity>.mapToUiItem() = this.map { item ->
    GifUiItem(
        title = item.title,
        link = item.link,
    )
}