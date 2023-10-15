package com.example.gifapp.data.repository

import android.util.Log
import com.example.gifapp.domain.entities.GifUiItem
import com.example.gifapp.data.repository.GifsFetchingResponse.EmptyResponseError
import com.example.gifapp.data.repository.GifsFetchingResponse.GifsFetchingSuccess
import com.example.gifapp.data.repository.GifsFetchingResponse.LoadingError
import com.example.gifapp.data.database.GifsDao
import com.example.gifapp.domain.entities.GifDBEntity
import com.example.gifapp.domain.entities.GifNetItem
import com.example.gifapp.domain.GifsRepository
import com.example.gifapp.util.mapToDataBaseEntities
import com.example.gifapp.util.mapToUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val gifsNetSource: GifsNetSource,
    private val gifsDao: GifsDao,
) : GifsRepository {

    override suspend fun deleteAllGifs() {
        Log.i("mytag*", "REPO deleteAllGifs: START")
        gifsDao.deleteAllGifs()
        Log.i("mytag*", "REPO deleteAllGifs: END")
    }

    override val gifFlow: Flow<List<GifUiItem>>
        get() = gifsDao.getDBGifs().map { dbList ->
            dbList.mapToUiItem()
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
