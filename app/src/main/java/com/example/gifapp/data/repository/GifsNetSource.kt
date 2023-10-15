package com.example.gifapp.data.repository

import com.example.gifapp.domain.entities.GifNetItem

interface GifsNetSource {
    suspend fun getGifsFromNet(offset: Int): Result<List<GifNetItem>>
}
