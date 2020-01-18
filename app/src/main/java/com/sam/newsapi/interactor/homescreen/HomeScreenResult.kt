package com.sam.newsapi.interactor.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.NewsModel
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.interactor.base.MviResult

sealed class HomeScreenResult : MviResult {
    sealed class LoadHomeScreenResult : HomeScreenResult() {
        data class Success(
            val sectionList: List<Category>
        ) : LoadHomeScreenResult()

        data class Failure(val error: Throwable) : LoadHomeScreenResult()
    }

    sealed class LoadNewsListResult : HomeScreenResult() {
        data class Success(
            val data: NewsModel
        ) : LoadNewsListResult()

        data class Failure(val error: Throwable) : LoadNewsListResult()

        object InFlight : LoadNewsListResult()
    }

    data class NewsItemClickResult(
        val nyNewsListItem: Article
    ) : HomeScreenResult()

    object DialogCancelResult : HomeScreenResult()
}