package com.example.gifapp.data.repository

import com.example.gifapp.domain.entities.GifDBEntity
import kotlinx.coroutines.flow.Flow

interface GifsLocalSource {
    val gifFlow: Flow<List<GifDBEntity>>
    suspend fun upsertGifsToDB(bDEntities: List<GifDBEntity>)
    suspend fun deleteAllGifs()
}
