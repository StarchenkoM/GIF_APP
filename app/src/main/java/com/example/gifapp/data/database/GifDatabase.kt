package com.example.gifapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gifapp.domain.entities.GifDBEntity

//@TypeConverters(TypeConverter::class)
@Database(
    entities = [GifDBEntity::class],
    version = DbConstants.ROOM_DATABASE_VERSION
)
abstract class GifDatabase : RoomDatabase() {

    abstract fun gifsDao(): GifsDao
}
