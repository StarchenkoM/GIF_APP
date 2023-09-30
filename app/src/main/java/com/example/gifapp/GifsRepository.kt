package com.example.gifapp

import com.example.gifapp.ui.home.GifUiModel
import kotlinx.coroutines.flow.Flow

interface GifsRepository {
    fun getGifs(): Flow<List<GifUiModel>>
}