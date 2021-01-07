package com.katdmy.android.myfirstkotlinapp.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

object RetrofitClient {

    private const val MainServer = "https://api.themoviedb.org/3/"
    private const val TMDB_API_KEY = "37d023007af6569b99e1ba7cad35a94b"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = OkHttpClient().newBuilder()
        //.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        // TmdpApiKeyInterceptor
        .addInterceptor {
            val request = it.request()
            val newUrl = request.url.newBuilder()
                .addQueryParameter("api_key", TMDB_API_KEY)
                .build()
            it.proceed(
                request.newBuilder()
                    .url(newUrl)
                    .build()
            )
        }
        .build()

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MainServer)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val tmdbApi: TmdbApi = retrofit.create()

}