package com.example.gifapp

import android.util.Log
import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val gifsNetSource: GifsNetSource,
    private val gifsDao: GifsDao,
    private val mapper: GifsMapper,

    ) : GifsRepository {


    override suspend fun getGifs(query: String): Flow<List<GifUiItem>> {
        val result = gifsNetSource.getGifsFromNet(query).onEach {
            Log.i("mytag", "REPO getGifs: netItems.size = ${it.size}")
        }.map {
            val t = mutableListOf<GifUiItem>()
            val temp = it.forEach {
                t.add(mapper.convertNetToUiItem(it))
            }
            Log.i("mytag", "REPO getGifs: uiItems.size = ${t.size}")

            t
        }

        Log.i("mytag", "REPO getGifs: result = ${result.first()}")

        return result
    }
}