package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow

interface GifsRepository {
    suspend fun getGifs(query: String): GifsFetchingResponse
    suspend fun deleteSomeGifs()
    suspend fun deleteAllGifs()
    val gifFlow: Flow<List<GifUiItem>>
//    val gifFlowResponse: Flow<GifsResponse>
}