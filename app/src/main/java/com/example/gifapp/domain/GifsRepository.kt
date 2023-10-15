package com.example.gifapp.domain

import com.example.gifapp.data.repository.GifsFetchingResponse
import com.example.gifapp.domain.entities.GifUiItem
import kotlinx.coroutines.flow.Flow

interface GifsRepository {
    val gifFlow: Flow<List<GifUiItem>>
    suspend fun getGifs(offset: Int): GifsFetchingResponse
    suspend fun deleteAllGifs()
}