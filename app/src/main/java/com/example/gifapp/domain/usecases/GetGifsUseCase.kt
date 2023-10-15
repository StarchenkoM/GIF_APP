package com.example.gifapp.domain.usecases

import com.example.gifapp.data.repository.GifsFetchingResponse
import com.example.gifapp.domain.entities.GifUiItem
import kotlinx.coroutines.flow.Flow

interface GetGifsUseCase {
    suspend fun getGifs(offset: Int): GifsFetchingResponse
    suspend fun deleteGifs()
    val gifFlow: Flow<List<GifUiItem>>
}