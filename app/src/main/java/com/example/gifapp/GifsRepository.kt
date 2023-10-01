package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow

interface GifsRepository {
    suspend fun getGifs(): Flow<List<GifUiItem>>
    suspend fun getGifs(query: String): Flow<List<GifUiItem>>
}