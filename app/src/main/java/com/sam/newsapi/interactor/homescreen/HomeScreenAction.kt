package com.sam.newsapi.interactor.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.interactor.base.MviAction


sealed class HomeScreenAction : MviAction {
    object LoadHomeScreenAction : HomeScreenAction()

    data class CategoryClickAction(
        val category: String
    ) : HomeScreenAction()

    data class ArticleClickAction(
        val article: Article
    ) : HomeScreenAction()

    object DialogCancelAction : HomeScreenAction()

    data class RefreshAction(
        val categoryList: List<Category>
    ) : HomeScreenAction()
}