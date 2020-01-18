package com.sam.newsapi.presentation.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.presentation.base.MviIntent

sealed class HomeScreenIntent : MviIntent {
    object InitialIntent : HomeScreenIntent()

    data class OnArticleClickIntent(
        val article: Article
    ) : HomeScreenIntent()

    data class OnCategoryClickIntent(
        val category: Category
    ) : HomeScreenIntent()

    object ErrorDialogCancelIntent : HomeScreenIntent()

    data class RefreshIntent(
        val categoryList: List<Category>
    ) : HomeScreenIntent()
}