package com.example.gifapp

import kotlinx.coroutines.flow.Flow

interface GifsRepository {
    suspend fun getGifs(offset: Int): GifsFetchingResponse
    suspend fun deleteAllGifs()
    val gifFlow: Flow<List<GifUiItem>>
}