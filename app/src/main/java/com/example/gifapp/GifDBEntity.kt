package com.example.gifapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gifs_table")
data class GifDBEntity(
    @PrimaryKey
    var id: String/* = ""*/,
    val title: String,
    val link: String,
)
