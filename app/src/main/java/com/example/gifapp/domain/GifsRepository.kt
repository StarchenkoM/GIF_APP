package com.example.gifapp.domain

import com.example.gifapp.data.repository.GifsFetchingResponse
import com.example.gifapp.domain.entities.GifUiItem
import kotlinx.coroutines.flow.Flow

interface GifsRepository {
    suspend fun getGifs(offset: Int): GifsFetchingResponse
    suspend fun deleteAllGifs()
    val gifFlow: Flow<List<GifUiItem>>
}