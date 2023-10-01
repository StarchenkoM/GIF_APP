package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGifsUseCaseImpl @Inject constructor(
    private val gifsRepository: GifsRepository
) : GetGifsUseCase {

    override suspend fun getGifs(query: String): Flow<List<GifUiItem>> {
        return gifsRepository.getGifs(query)
    }
}