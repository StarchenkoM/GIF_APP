package com.example.gifapp

import android.util.Log
import com.example.gifapp.database.GifsDao
import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val gifsNetSource: GifsNetSource,
    private val gifsDao: GifsDao,
    private val mapper: GifsMapper,
) : GifsRepository {

    override suspend fun getGifs(query: String): Flow<List<GifUiItem>> {
        gifsNetSource.getGifsFromNet(query).onEach {
            it.forEach { netItem ->
                gifsDao.upsertGif(mapper.mapToDataBaseItem(netItem))
            }
        }
        val result = gifsDao.getDBGifs().map { dbList ->
            dbList.map { mapper.mapToUiItem(it) }
        }
        return result
    }

}