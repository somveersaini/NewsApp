package com.sam.newsapi.presentation.detailspage

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.presentation.base.MviIntent

sealed class DetailsPageIntent : MviIntent {

    data class InitialIntent(
        val article: Article
    ) : DetailsPageIntent()

    data class OnNewsUrlClickIntent(
        val url: String
    ) : DetailsPageIntent()

    object ErrorDialogCancelIntent : DetailsPageIntent()
}