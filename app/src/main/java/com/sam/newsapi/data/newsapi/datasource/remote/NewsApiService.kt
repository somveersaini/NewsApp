package com.sam.newsapi.data.newsapi.datasource.remote

import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {

    @GET("v2/top-headlines")
    fun getNews(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("country") country: String
    ): Single<NewsModel>

}