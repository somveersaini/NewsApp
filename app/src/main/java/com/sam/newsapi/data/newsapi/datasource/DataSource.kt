package com.sam.newsapi.data.newsapi.datasource

import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single

interface DataSource {
    fun getNewsData(category: String): Single<NewsModel>
}