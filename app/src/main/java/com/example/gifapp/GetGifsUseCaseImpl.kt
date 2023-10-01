package com.example.gifapp

import android.util.Log
import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetGifsUseCaseImpl @Inject constructor(
    private val gifsRepository: GifsRepository
) : GetGifsUseCase {

    override suspend fun getGifs(): Flow<List<GifUiItem>> {
        val result = gifsRepository.getGifs()
        Log.i("mytag", "USECASE getGifs: result.first() = ${result.first().map { it.title }}")

        return result
    }

    override suspend fun getGifs(query: String): Flow<List<GifUiItem>> {
        return gifsRepository.getGifs(query)
    }
}