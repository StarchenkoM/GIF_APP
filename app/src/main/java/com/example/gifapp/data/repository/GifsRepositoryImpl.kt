package com.example.gifapp.data.repository

import com.example.gifapp.data.repository.GifsFetchingResponse.EmptyResponseError
import com.example.gifapp.data.repository.GifsFetchingResponse.GifsFetchingSuccess
import com.example.gifapp.data.repository.GifsFetchingResponse.LoadingError
import com.example.gifapp.domain.GifsRepository
import com.example.gifapp.domain.entities.GifDBEntity
import com.example.gifapp.domain.entities.GifNetItem
import com.example.gifapp.domain.entities.GifUiItem
import com.example.gifapp.util.mapToDataBaseEntities
import com.example.gifapp.util.mapToUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
    private val gifsNetSource: GifsNetSource,
    private val gifsLocalSource: GifsLocalSource,
) : GifsRepository {

    override val gifFlow: Flow<List<GifUiItem>>
        get() = gifsLocalSource.gifFlow.map { dbList ->
            dbList.mapToUiItem()
        }

    override suspend fun getGifs(offset: Int): GifsFetchingResponse {
        gifsNetSource.getGifsFromNet(offset).onSuccess { netItems ->
            return handleSuccessResponse(netItems)
        }
        return LoadingError
    }

    override suspend fun deleteAllGifs() {
        gifsLocalSource.deleteAllGifs()
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
            gifsLocalSource.upsertGifsToDB(bDEntities)
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
