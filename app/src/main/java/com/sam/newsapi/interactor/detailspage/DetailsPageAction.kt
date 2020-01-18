package com.sam.newsapi.interactor.detailspage

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.interactor.base.MviAction


sealed class DetailsPageAction : MviAction {
    data class LoadDetailsPageAction(
        val article: Article
    ) : DetailsPageAction()

    data class OnNewsUrlClickAction(
        val url: String
    ) : DetailsPageAction()

    object DialogCancelAction : DetailsPageAction()
}