package com.example.gifapp.di

import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.GetGifsUseCaseImpl
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
    fun provideGifsRepository(): GifsRepository = GifsRepositoryImpl()

}