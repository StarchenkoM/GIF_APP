package com.example.gifapp

import android.util.Log
import com.example.gifapp.api.RetrofitInstance

class GifsNetSourceImpl : GifsNetSource {
    override suspend fun getGifsFromNet(query: String): Result<List<GifNetItem>> {
        Log.i("mytag*", "NET_SOURCE NET: START")
        Log.i("mytag", "NET_SOURCE getGifsFromNet: query = $query")

        return kotlin.runCatching {
            val response = if (query.isEmpty()) {
                RetrofitInstance.api.getGifs()
            } else {
                RetrofitInstance.api.getGifs(query = query)
            }
            Log.i("mytag*", "NET_SOURCE NET: END")
            response.body()?.gifs ?: emptyList()
        }
    }

}