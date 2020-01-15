package com.sam.newsapi.data.newsapi

import com.sam.newsapi.data.newsapi.datasource.local.LocalDataSource
import com.sam.newsapi.data.newsapi.datasource.remote.RemoteDataSource
import com.sam.newsapi.data.newsapi.model.NewsModel
import io.reactivex.Observable
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
) : NewsRepository {

    override fun getNewsData(category: String): Observable<NewsModel> {
        return Observable.mergeDelayError(
            remoteDataSource.getNewsData(category)
                .doAfterSuccess {
                    localDataSource.insertNewsData(
                        NewsModel(category, it.articles)
                    )
                }.toObservable(),
            localDataSource.getNewsData(category).toObservable()
        )
    }
}
