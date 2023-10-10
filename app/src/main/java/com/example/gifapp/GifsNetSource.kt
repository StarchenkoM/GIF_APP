package com.example.gifapp

interface GifsNetSource {
    suspend fun getGifsFromNet(offset: Int): Result<List<GifNetItem>>
}
