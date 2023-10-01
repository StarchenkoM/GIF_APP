package com.example.gifapp

import kotlinx.coroutines.flow.Flow

interface GifsDao {
    fun getDBGifs(): Flow<List<GifDBItem>>
}
