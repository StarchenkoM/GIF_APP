package com.example.gifapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gifapp.GifDBEntity

//@TypeConverters(TypeConverter::class)
@Database(
    entities = [GifDBEntity::class],
    version = DbConstants.ROOM_DATABASE_VERSION
)
abstract class GifDatabase : RoomDatabase() {

    abstract fun gifsDao(): GifsDao
}
