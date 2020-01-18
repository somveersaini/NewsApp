package com.sam.newsapi.interactor.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.interactor.base.MviAction


sealed class HomeScreenAction : MviAction {
    object LoadHomeScreenAction : HomeScreenAction()

    data class LoadSectionNewsAction(
        val section: String
    ) : HomeScreenAction()

    data class OnNewsItemClickAction(
        val nyNewsListItem: Article
    ) : HomeScreenAction()

    object DialogCancelAction : HomeScreenAction()

    data class RefreshAction(
        val sectionList: List<Category>
    ) : HomeScreenAction()
}