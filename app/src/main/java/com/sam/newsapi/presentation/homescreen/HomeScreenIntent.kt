package com.sam.newsapi.presentation.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.presentation.base.MviIntent

sealed class HomeScreenIntent : MviIntent {
    object InitialIntent : HomeScreenIntent()

    data class OnNewsItemClickIntent(
        val nyNewsListItem: Article
    ) : HomeScreenIntent()

    data class OnSectionItemClickIntent(
        val category: Category
    ) : HomeScreenIntent()

    object ErrorDailogCancelIntent : HomeScreenIntent()

    data class RefreshIntent(
        val sectionList: List<Category>
    ) : HomeScreenIntent()
}