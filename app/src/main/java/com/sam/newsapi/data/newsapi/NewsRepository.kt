package com.sam.newsapi.data.newsapi

import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single


interface NewsRepository {

    fun getNewsData(category: String): Single<NewsModel>

    fun getCategoryList(): Single<List<Category>>
}