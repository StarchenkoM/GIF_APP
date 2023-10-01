package com.example.gifapp

import kotlinx.coroutines.flow.Flow

class GifsDaoImpl : GifsDao {
    override fun getDBGifs(): Flow<List<GifDBItem>> {
        TODO("Not yet implemented")
    }
}