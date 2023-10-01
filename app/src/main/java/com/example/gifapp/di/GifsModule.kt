package com.example.gifapp.di

import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.GetGifsUseCaseImpl
import com.example.gifapp.GifsDao
import com.example.gifapp.GifsDaoImpl
import com.example.gifapp.GifsMapper
import com.example.gifapp.GifsMapperImpl
import com.example.gifapp.GifsNetSource
import com.example.gifapp.GifsNetSourceImpl
import com.example.gifapp.GifsRepository
import com.example.gifapp.GifsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GifsModule {

    @Provides
    fun provideGetGifsUseCase(gifsRepository: GifsRepository): GetGifsUseCase =
        GetGifsUseCaseImpl(gifsRepository)

    @Singleton
    @Provides
    fun provideGifsRepository(
        gifsNetSource: GifsNetSource,
        gifsDao: GifsDao,
        gifsMapper: GifsMapper,
    ): GifsRepository =
        GifsRepositoryImpl(gifsNetSource, gifsDao, gifsMapper)

    @Provides
    fun provideGifsNetSource(): GifsNetSource = GifsNetSourceImpl()

    @Provides
    fun provideGifsDao(): GifsDao = GifsDaoImpl()

    @Provides
    fun provideGifsMapper(): GifsMapper = GifsMapperImpl()

}