package com.example.gifapp

import android.util.Log
import com.example.gifapp.api.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class GifsNetSourceImpl : GifsNetSource {
    override suspend fun getGifsFromNet(query: String): Flow<List<GifNetItem>> {
        Log.i("mytag", "NET_SOURCE getGifsFromNet: query = $query")

        val response = if (query.isEmpty()) {
            RetrofitInstance.api.getGifs()
        } else {
            RetrofitInstance.api.getGifs(query = query)
        }
        val body = response.body()

        Log.i(
            "mytag",
            "NET_SOURCE getGifsFromNet: response.isSuccessful = ${response.isSuccessful} && body != null ${body != null}"
        )
        return if (response.isSuccessful && body != null) {
            val res = body.gifs.map {
                GifNetItem(
                    gifName = it.title,
                    gifLink = it.images.original.url
                )
            }
            Log.i(
                "mytag",
                "NET_SOURCE getGifsFromNet: res =${res.size} res.titles = ${
                    res.map {
                        it.gifName.substring(
                            0,
                            3
                        )
                    }
                } \n")

            flowOf(res)
        } else {
            Log.i("mytag", "NET_SOURCE getGifsFromNet: EMPTY FLOW")

            emptyFlow()
        }
    }

}