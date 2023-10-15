package com.example.gifapp.domain.usecases

import android.util.Log
import com.example.gifapp.data.repository.GifsFetchingResponse
import com.example.gifapp.domain.GifsRepository
import com.example.gifapp.domain.entities.GifUiItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGifsUseCaseImpl @Inject constructor(
    private val gifsRepository: GifsRepository
) : GetGifsUseCase {

    override suspend fun getGifs(offset: Int): GifsFetchingResponse {
        return gifsRepository.getGifs(offset)
    }

    override suspend fun deleteGifs() {
        Log.i("mytag**", "USE_CASE: deleteGifs")
//        gifsRepository.deleteSomeGifs()
        gifsRepository.deleteAllGifs()
    }

    override val gifFlow: Flow<List<GifUiItem>>
        get() = gifsRepository.gifFlow
}