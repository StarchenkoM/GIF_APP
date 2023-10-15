package com.example.gifapp.data.datasource

import com.example.gifapp.data.api.GifsApi
import com.example.gifapp.data.repository.GifsNetSource
import com.example.gifapp.domain.entities.GifNetItem
import javax.inject.Inject

class GifsNetSourceImpl @Inject constructor(
    private val gifsApi: GifsApi
) : GifsNetSource {
    override suspend fun getGifsFromNet(offset: Int): Result<List<GifNetItem>> {
        return kotlin.runCatching {
            gifsApi.getGifs(offset = offset).body()?.gifs ?: emptyList()
        }
    }
}