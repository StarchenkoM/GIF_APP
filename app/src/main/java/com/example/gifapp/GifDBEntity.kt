package com.example.gifapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gifs_table")
data class GifDBEntity(
    @PrimaryKey
    var id: String,
    val title: String,
    val link: String,
)

fun List<GifNetItem>.mapToDataBaseEntities() = this.map { item ->
    GifDBEntity(
        id = item.id,
        title = item.title,
        link = item.images.imageDetails.url,
    )
}
