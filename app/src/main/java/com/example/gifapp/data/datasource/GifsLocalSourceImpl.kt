package com.example.gifapp.data.datasource

import com.example.gifapp.data.database.GifsDao
import com.example.gifapp.data.repository.GifsLocalSource
import com.example.gifapp.domain.entities.GifDBEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GifsLocalSourceImpl @Inject constructor(
    private val gifsDao: GifsDao,
) : GifsLocalSource {

    override val gifFlow: Flow<List<GifDBEntity>>
        get() = gifsDao.getDBGifs()

    override suspend fun upsertGifsToDB(bDEntities: List<GifDBEntity>) {
        gifsDao.upsertGifs(bDEntities)
    }

    override suspend fun deleteAllGifs() {
        gifsDao.deleteAllGifs()
    }



}