package com.example.gifapp

import android.util.Log
import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGifsUseCaseImpl @Inject constructor(
    private val gifsRepository: GifsRepository
) : GetGifsUseCase {

    override suspend fun getGifs(query: String) {
        gifsRepository.getGifs(query)
    }

    override suspend fun deleteGifs() {
        Log.i("mytag**", "USE_CASE: deleteGifs")
        gifsRepository.deleteSomeGifs()
    }

    override val gifFlow: Flow<List<GifUiItem>>
        get() = gifsRepository.gifFlow
}