package com.sam.newsapi.data.newsapi

import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Observable


interface NewsRepository {

    fun getNewsData(category: String): Observable<NewsModel>
}