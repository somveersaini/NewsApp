package com.sam.newsapi.interactor.detailspage

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.interactor.base.MviResult

sealed class DetailsPageResult : MviResult {
    sealed class LoadDetailsPageResult : DetailsPageResult() {
        data class Success(
            val article: Article
        ) : LoadDetailsPageResult()

        object InFlight : LoadDetailsPageResult()
    }

    sealed class OnNewsUrlClickResult : DetailsPageResult() {
        data class Success(
            val url: String
        ) : OnNewsUrlClickResult()

        data class Failure(
            val error: Throwable
        ) : OnNewsUrlClickResult()
    }

    object DialogCancelResult : DetailsPageResult()
}