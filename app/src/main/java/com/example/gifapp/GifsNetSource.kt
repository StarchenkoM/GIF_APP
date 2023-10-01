package com.example.gifapp

import kotlinx.coroutines.flow.Flow

interface GifsNetSource {
    suspend fun getGifsFromNet(query: String = ""): Flow<List<GifNetItem>>
}
