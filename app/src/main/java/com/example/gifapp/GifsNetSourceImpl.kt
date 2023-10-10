package com.example.gifapp

import android.util.Log
import com.example.gifapp.api.RetrofitInstance

class GifsNetSourceImpl : GifsNetSource {
    override suspend fun getGifsFromNet(offset: Int): Result<List<GifNetItem>> {
        Log.i("mytag*****", "NET_SOURCE NET: START offset = $offset")

        // TODO: add dependency injection
        return kotlin.runCatching {
            val response = RetrofitInstance.api.getGifs(offset = offset)
            Log.i("mytag*", "NET_SOURCE NET: END")
            response.body()?.gifs ?: emptyList()
//            emptyList()
        }
    }

}