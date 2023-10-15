package com.example.gifapp.di

import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.GetGifsUseCaseImpl
import com.example.gifapp.GifsNetSource
import com.example.gifapp.GifsNetSourceImpl
import com.example.gifapp.GifsRepository
import com.example.gifapp.GifsRepositoryImpl
import com.example.gifapp.api.GifsApi
import com.example.gifapp.database.GifsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GifsModule {

    @Provides
    fun provideGetGifsUseCase(gifsRepository: GifsRepository): GetGifsUseCase =
        GetGifsUseCaseImpl(gifsRepository)

    @Provides
    fun provideGifsRepository(
        gifsNetSource: GifsNetSource,
        gifsDao: GifsDao,
    ): GifsRepository =
        GifsRepositoryImpl(gifsNetSource, gifsDao)

    @Provides
    fun provideGifsNetSource(gifsApi: GifsApi): GifsNetSource = GifsNetSourceImpl(gifsApi)

}