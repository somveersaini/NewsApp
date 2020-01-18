package com.sam.newsapi.presentation.homescreen.adapter


import androidx.recyclerview.widget.DiffUtil
import com.sam.newsapi.data.newsapi.model.Category

class CategoryListDiffUtil(
    private val oldList: List<Category>,
    private val newList: List<Category>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name === newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (isChecked, name) = oldList[oldPosition]
        val (isChecked1, name1) = newList[newPosition]

        return isChecked == isChecked1 && name == name1
    }
}