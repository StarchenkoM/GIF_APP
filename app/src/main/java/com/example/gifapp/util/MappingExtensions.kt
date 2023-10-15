package com.example.gifapp.util

import com.example.gifapp.domain.entities.GifDBEntity
import com.example.gifapp.domain.entities.GifNetItem
import com.example.gifapp.domain.entities.GifUiItem

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