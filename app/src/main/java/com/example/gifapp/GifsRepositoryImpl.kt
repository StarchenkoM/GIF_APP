package com.example.gifapp

import android.util.Log
import com.example.gifapp.database.GifsDao
import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val gifsNetSource: GifsNetSource,
    private val gifsDao: GifsDao,
    private val mapper: GifsMapper,
) : GifsRepository {

    override suspend fun deleteSomeGifs() {
        Log.i("mytag*", "REPO deleteAll: START")
        val deletionList = listOf(
            "3o7527pa7qs9kCG78A",
            "4Zo41lhzKt6iZ8xff9",
            "ЄgFW9rRpOkMRBY2KF6s",
            "kiBcwEXegBTACmVOnE",
            "QvBoMEcQ7DQXK",
            "xUA7aQaXbhnkX4znm8",
        )
        deletionList.forEach {
            gifsDao.deleteGif(it)
        }
//        gifsDao.deleteAllGifs()
        Log.i("mytag*", "REPO deleteAll: END")

    }

    override val gifFlow: Flow<List<GifUiItem>>
        get() = gifsDao.getDBGifs().map { dbList ->
            dbList.map { mapper.mapToUiItem(it) }
        }



    override suspend fun getGifs(query: String) {
//        deleteAll()
        Log.i("mytag*", "REPO getGifs: START")
        val d = gifsNetSource.getGifsFromNet(query)

        d.onSuccess { netItems ->
            if (netItems.isNotEmpty()) {
                netItems.forEach { netItem ->
                    gifsDao.upsertGif(mapper.mapToDataBaseItem(netItem))
                }
            }
        }.onFailure {

        }

    }


}