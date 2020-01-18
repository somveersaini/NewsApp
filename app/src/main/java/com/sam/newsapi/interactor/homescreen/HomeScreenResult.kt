package com.sam.newsapi.interactor.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.data.newsapi.model.NewsModel
import com.sam.newsapi.interactor.base.MviResult

sealed class HomeScreenResult : MviResult {
    sealed class LoadHomeScreenResult : HomeScreenResult() {
        data class Success(
            val category: List<Category>
        ) : LoadHomeScreenResult()

        data class Failure(val error: Throwable) : LoadHomeScreenResult()
    }

    sealed class LoadCategoryArticlesResult : HomeScreenResult() {
        data class Success(
            val data: NewsModel
        ) : LoadCategoryArticlesResult()

        data class Failure(val error: Throwable) : LoadCategoryArticlesResult()

        object InFlight : LoadCategoryArticlesResult()
    }

    data class ArticleClickResult(
        val article: Article
    ) : HomeScreenResult()

    object DialogCancelResult : HomeScreenResult()
}