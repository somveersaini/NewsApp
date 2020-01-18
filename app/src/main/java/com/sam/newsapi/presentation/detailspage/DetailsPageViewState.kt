package com.sam.newsapi.presentation.detailspage

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.presentation.base.MviViewState

data class DetailsPageViewState(
    val isLoading: Boolean,
    val article: Article?,
    val error: Throwable?
) : MviViewState {

    companion object {
        fun idle(): DetailsPageViewState {
            return DetailsPageViewState(
                isLoading = true,
                article = null,
                error = null
            )
        }
    }
}