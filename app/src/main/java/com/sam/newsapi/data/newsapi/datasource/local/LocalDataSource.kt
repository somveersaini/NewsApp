package com.sam.newsapi.data.newsapi.datasource.local

import com.sam.newsapi.data.newsapi.datasource.DataSource
import com.sam.newsapi.data.newsapi.datasource.local.db.NewsDatabase
import com.sam.newsapi.data.newsapi.datasource.local.db.entity.NewsEntity
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val newsDatabase: NewsDatabase
) : DataSource {
    override fun getNewsData(category: String): Single<NewsModel> {
        val newsEntitySingle = newsDatabase.newsDao().getNewsEntityByNewsId(category)
        return newsEntitySingle.map { NewsModel(category, it.news_list) }
    }

    fun insertNewsData(data: NewsModel) {
        newsDatabase.newsDao().insert(NewsEntity(data.key, data.articles))
    }
}