package com.example.gifapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.gifapp.GifDBEntity
import com.example.gifapp.database.DbConstants.GIFS_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface GifsDao {

    @Upsert
    suspend fun upsertGif(gif: GifDBEntity)

    @Query("SELECT * FROM $GIFS_TABLE_NAME")
    fun getDBGifs(): Flow<List<GifDBEntity>>
}
