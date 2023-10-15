package com.example.gifapp.di

import com.example.gifapp.domain.usecases.GetGifsUseCase
import com.example.gifapp.domain.usecases.GetGifsUseCaseImpl
import com.example.gifapp.data.repository.GifsNetSource
import com.example.gifapp.data.datasource.GifsNetSourceImpl
import com.example.gifapp.domain.GifsRepository
import com.example.gifapp.data.repository.GifsRepositoryImpl
import com.example.gifapp.data.api.GifsApi
import com.example.gifapp.data.database.GifsDao
import com.example.gifapp.data.datasource.GifsLocalSourceImpl
import com.example.gifapp.data.repository.GifsLocalSource
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
        gifsLocalSource: GifsLocalSource,
    ): GifsRepository = GifsRepositoryImpl(gifsNetSource, gifsLocalSource)

    @Provides
    fun provideGifsNetSource(gifsApi: GifsApi): GifsNetSource = GifsNetSourceImpl(gifsApi)

    @Provides
    fun provideGifsLocalSource(gifsDao: GifsDao): GifsLocalSource = GifsLocalSourceImpl(gifsDao)

}