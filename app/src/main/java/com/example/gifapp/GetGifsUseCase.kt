package com.example.gifapp

import kotlinx.coroutines.flow.Flow

interface GetGifsUseCase {
    suspend fun getGifs(offset: Int): GifsFetchingResponse
    suspend fun deleteGifs()
    val gifFlow: Flow<List<GifUiItem>>
}