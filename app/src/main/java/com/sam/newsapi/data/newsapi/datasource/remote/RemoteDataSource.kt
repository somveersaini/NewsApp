package com.sam.newsapi.data.newsapi.datasource.remote

import com.sam.newsapi.BuildConfig
import com.sam.newsapi.data.newsapi.datasource.DataSource
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    val newsApiService: NewsApiService
) : DataSource {

    override fun getNewsData(newsId: String): Single<NewsModel> {
        return newsApiService.getNews(
            newsId,
            BuildConfig.NewsApiKey
        )
    }
}