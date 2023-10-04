package com.example.gifapp

interface GifsNetSource {
    suspend fun getGifsFromNet(query: String = ""): Result<List<GifNetItem>>
}
