package com.sam.newsapi.presentation.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.presentation.base.MviViewEffect

sealed class HomeScreenViewEffect : MviViewEffect {
    data class RefreshEffect(
        val sectionList: List<Category>
    ) : HomeScreenViewEffect()

    data class OpenDetailsPageEffect(
        val article: Article
    ) : HomeScreenViewEffect()
}