package com.sam.newsapi.presentation.homescreen

import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.presentation.base.MviViewState

data class HomeScreenViewState(
    val isRefreshing: Boolean,
    val articleList: List<Article>?,
    var categoryList: List<Category>?,
    val error: Throwable?
) : MviViewState {

    fun updateCategory(category: String): HomeScreenViewState {
        val tempList = mutableListOf<Category>()
        categoryList?.let { item ->
            item.forEach {
                tempList.add(
                    Category(
                        category == it.name,
                        it.name
                    )
                )
            }
            categoryList = tempList
        }
        return this
    }

    companion object {
        fun idle(): HomeScreenViewState {
            return HomeScreenViewState(
                isRefreshing = true,
                articleList = null,
                categoryList = null,
                error = null
            )
        }
    }
}