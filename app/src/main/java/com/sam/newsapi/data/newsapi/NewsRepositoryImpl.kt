package com.sam.newsapi.data.newsapi

import com.sam.newsapi.data.newsapi.datasource.local.LocalDataSource
import com.sam.newsapi.data.newsapi.datasource.remote.RemoteDataSource
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Single
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NewsRepository {

    override fun getNewsData(category: String): Single<NewsModel> {
        return Single.mergeDelayError(
            remoteDataSource.getNewsData(category)
                .doAfterSuccess {
                    localDataSource.insertNewsData(
                        NewsModel(category, it.articles)
                    )
                },
            localDataSource.getNewsData(category)
        ).firstOrError()
    }

    override fun getCategoryList(): Single<List<Category>> {
        return Single.just(
            mutableListOf(
                Category(true,"business"),
                Category(false, "general"),
                Category(false, "health"),
                Category(false, "science"),
                Category(false, "sports"),
                Category(false, "technology"),
                Category(false, "entertainment")
            )
        )
    }


}
