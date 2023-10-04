package com.example.gifapp.api


import com.example.gifapp.GifsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GifsApi {

    @GET("gifs/search?api_key=YGHnKKBGSydS6nSt6WAoUcICWwmgCfvL")
    suspend fun getGifs(
        @Query("q") query: String = DEFAULT_QUERY,
        @Query("limit") limit: Int = DEFAULT_LIMIT,
        @Query("offset") offset: Int = DEFAULT_OFFSET,
        @Query("rating") rating: String = DEFAULT_RATING,
        @Query("lang") language: String = DEFAULT_LANGUAGE,
//    ): Flow<GifsApiResponse>
    ): Response<GifsApiResponse>

    companion object {
        const val DEFAULT_QUERY = "dog"
        const val DEFAULT_LIMIT = 11
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_RATING = "g"
        const val DEFAULT_LANGUAGE = "en"
    }

}