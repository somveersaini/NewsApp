package com.sam.newsapi.presentation.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.presentation.base.MviViewState

data class HomeScreenViewState(
    val isRefreshing: Boolean,
    val newsList: List<Article>?,
    var sectionList: List<Category>?,
    val error: Throwable?
) : MviViewState {

    fun updateSection(section: String): HomeScreenViewState {
        val tempList = mutableListOf<Category>()
        sectionList?.let { item ->
            item.forEach {
                tempList.add(
                    Category(
                        section == it.name,
                        it.name
                    )
                )
            }
            sectionList = tempList
        }
        return this
    }

    companion object {
        fun idle(): HomeScreenViewState {
            return HomeScreenViewState(
                isRefreshing = true,
                newsList = null,
                sectionList = null,
                error = null
            )
        }
    }
}