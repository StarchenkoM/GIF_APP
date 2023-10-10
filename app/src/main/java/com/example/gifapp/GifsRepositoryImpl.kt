package com.example.gifapp

import android.util.Log
import com.example.gifapp.GifsFetchingResponse.EmptyResponseError
import com.example.gifapp.GifsFetchingResponse.GifsFetchingSuccess
import com.example.gifapp.GifsFetchingResponse.LoadingError
import com.example.gifapp.database.GifsDao
import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val gifsNetSource: GifsNetSource,
    private val gifsDao: GifsDao,
    private val mapper: GifsMapper,
) : GifsRepository {

    override suspend fun deleteSomeGifs() {
        Log.i("mytag*", "REPO deleteSomeGifs: START")
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
        Log.i("mytag*", "REPO deleteSomeGifs: END")

    }

    override suspend fun deleteAllGifs() {
        Log.i("mytag*", "REPO deleteAllGifs: START")
        gifsDao.deleteAllGifs()
        Log.i("mytag*", "REPO deleteAllGifs: END")
    }

    override val gifFlow: Flow<List<GifUiItem>>
        get() = gifsDao.getDBGifs().map { dbList ->
            dbList.map { mapper.mapToUiItem(it) }
        }

    override suspend fun getGifs(offset: Int): GifsFetchingResponse {
        Log.i("mytag*", "REPO getGifs: START")
        gifsNetSource.getGifsFromNet(offset).onSuccess { netItems ->
            return handleSuccessResponse(netItems)
        }
        Log.i("mytag*", "REPO getGifs: END")
        return LoadingError
    }

    private suspend fun handleSuccessResponse(netItems: List<GifNetItem>): GifsFetchingResponse {
        return if (netItems.isEmpty()) {
            EmptyResponseError
        } else {
            upsertGifsToDB(netItems.mapToDataBaseEntities())
        }
    }

    private suspend fun upsertGifsToDB(bDEntities: List<GifDBEntity>): GifsFetchingResponse {
        return try {
            gifsDao.upsertGifs(bDEntities)
            GifsFetchingSuccess
        } catch (ex: Exception) {
            LoadingError
        }
    }

}

sealed class GifsFetchingResponse {
    data object GifsFetchingSuccess : GifsFetchingResponse()
    data object EmptyResponseError : GifsFetchingResponse()
    data object LoadingError : GifsFetchingResponse()
}
