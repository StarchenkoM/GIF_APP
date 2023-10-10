package com.example.gifapp.di

import android.content.Context
import androidx.room.Room
import com.example.gifapp.GetGifsUseCase
import com.example.gifapp.GetGifsUseCaseImpl
import com.example.gifapp.GifsMapper
import com.example.gifapp.GifsMapperImpl
import com.example.gifapp.GifsNetSource
import com.example.gifapp.GifsNetSourceImpl
import com.example.gifapp.GifsRepository
import com.example.gifapp.GifsRepositoryImpl
import com.example.gifapp.api.Constants.BASE_URL
import com.example.gifapp.api.GifsApi
import com.example.gifapp.api.RequestInterceptor
import com.example.gifapp.database.GifDatabase
import com.example.gifapp.database.GifsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideGifsNetSource(gifsApi: GifsApi): GifsNetSource = GifsNetSourceImpl(gifsApi)


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

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(RequestInterceptor())
        val client = httpClient.build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideGifsApi(retrofit: Retrofit): GifsApi = retrofit.create(GifsApi::class.java)

}