package com.example.gifapp

import android.util.Log
import com.example.gifapp.api.GifsApi
import javax.inject.Inject

class GifsNetSourceImpl @Inject constructor(
    private val gifsApi: GifsApi
) : GifsNetSource {
    override suspend fun getGifsFromNet(offset: Int): Result<List<GifNetItem>> {
        Log.i("mytag*****", "NET_SOURCE NET: START offset = $offset")

        return kotlin.runCatching {
            gifsApi.getGifs(offset = offset).body()?.gifs ?: emptyList()
//            emptyList()
        }
    }

}