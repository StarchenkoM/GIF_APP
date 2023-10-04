package com.example.gifapp.di

import android.content.Context
import androidx.room.Room
import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.GetGifsUseCaseImpl
import com.example.gifapp.database.GifsDao
import com.example.gifapp.GifsMapper
import com.example.gifapp.GifsMapperImpl
import com.example.gifapp.GifsNetSource
import com.example.gifapp.GifsNetSourceImpl
import com.example.gifapp.GifsRepository
import com.example.gifapp.GifsRepositoryImpl
import com.example.gifapp.database.GifDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GifsModule {

    @Provides
    fun provideGetGifsUseCase(gifsRepository: GifsRepository): GetGifsUseCase =
        GetGifsUseCaseImpl(gifsRepository)

    //    @Singleton
    @Provides
    fun provideGifsRepository(
        gifsNetSource: GifsNetSource,
        gifsDao: GifsDao,
        gifsMapper: GifsMapper,
    ): GifsRepository =
        GifsRepositoryImpl(gifsNetSource, gifsDao, gifsMapper)

    @Provides
    fun provideGifsNetSource(): GifsNetSource = GifsNetSourceImpl()


    @Singleton
    @Provides
    fun provideGifDatabase(@ApplicationContext applicationContext: Context): GifDatabase = Room
        .databaseBuilder(applicationContext, GifDatabase::class.java, "Gif_database")
        .build()

    @Singleton
    @Provides
    fun provideGifsDao(database: GifDatabase): GifsDao = database.gifsDao()

    @Provides
    fun provideGifsMapper(): GifsMapper = GifsMapperImpl()

}