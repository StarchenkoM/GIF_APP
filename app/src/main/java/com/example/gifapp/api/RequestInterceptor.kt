package com.example.gifapp.api

import com.example.gifapp.api.Constants.API_KEY
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


class RequestInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedUrl: HttpUrl = request.url().newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val modifiedRequest = request.newBuilder()
            .url(modifiedUrl)
            .build()

        return chain.proceed(modifiedRequest)
    }

}