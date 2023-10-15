package com.example.gifapp.di

import android.content.Context
import androidx.room.Room
import com.example.gifapp.data.database.GifDatabase
import com.example.gifapp.data.database.GifsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GifsDBModule {

    @Singleton
    @Provides
    fun provideGifDatabase(@ApplicationContext applicationContext: Context): GifDatabase = Room
        .databaseBuilder(applicationContext, GifDatabase::class.java, "Gif_database")
        .build()

    @Singleton
    @Provides
    fun provideGifsDao(database: GifDatabase): GifsDao = database.gifsDao()

}