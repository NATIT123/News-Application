package com.example.newsapplication.Retrofit

import com.example.newsapplication.Models.NewsResponse
import com.example.newsapplication.Utils.Constants.Companion.API_KEY
import com.example.newsapplication.Utils.Constants.Companion.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        val apiService by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
                GsonConverterFactory.create(
                    gson
                )
            ).client(client).build().create(ApiService::class.java)
        }
    }

    @GET("top-headlines")
    fun getBreakingNews(
        @Query("country") country: String = "us",
        @Query("category") category: String = "business",
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Call<NewsResponse>


    @GET("everything")
    fun searchEverything(
        @Query("q") key: String,
        @Query("from") from: String = "2024-06-12",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsResponse>


}