package com.example.gifapp.di

import com.example.gifapp.api.Constants.BASE_URL
import com.example.gifapp.api.GifsApi
import com.example.gifapp.api.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GifsApiModule {

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