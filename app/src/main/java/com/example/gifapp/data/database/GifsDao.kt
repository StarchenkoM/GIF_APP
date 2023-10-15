package com.example.gifapp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.gifapp.domain.entities.GifDBEntity
import com.example.gifapp.data.database.DbConstants.GIFS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface GifsDao {

    @Upsert
    suspend fun upsertGif(gif: GifDBEntity)

    @Upsert
    suspend fun upsertGifs(gifs: List<GifDBEntity>)

    @Query("SELECT * FROM $GIFS_TABLE_NAME")
    fun getDBGifs(): Flow<List<GifDBEntity>>

    @Query("DELETE FROM $GIFS_TABLE_NAME")
    fun deleteAllGifs(): Int

}
