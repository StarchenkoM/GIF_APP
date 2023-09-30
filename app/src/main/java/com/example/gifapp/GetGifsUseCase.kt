package com.example.gifapp

import com.example.gifapp.ui.home.GifUiModel
import kotlinx.coroutines.flow.Flow

interface GetGifsUseCase {
    fun getGifs(): Flow<List<GifUiModel>>
}