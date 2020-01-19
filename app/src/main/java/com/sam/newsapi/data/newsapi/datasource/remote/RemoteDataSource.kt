package com.sam.newsapi.data.newsapi.datasource.remote

import com.sam.newsapi.BuildConfig
import com.sam.newsapi.data.newsapi.datasource.DataSource
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService
) : DataSource {

    override fun getNewsData(category: String): Single<NewsModel> {
        return newsApiService.getNews(
            category,
            BuildConfig.NewsApiKey,
            "in"
        )
    }
}