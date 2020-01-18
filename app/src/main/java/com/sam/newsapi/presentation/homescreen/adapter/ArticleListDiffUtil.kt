package com.sam.newsapi.presentation.homescreen.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sam.newsapi.data.newsapi.model.Article

class ArticleListDiffUtil(
    private val oldList: List<Article>,
    private val newList: List<Article>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return true
    }
}