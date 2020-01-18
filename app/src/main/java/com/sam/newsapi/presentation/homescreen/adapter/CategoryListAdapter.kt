package com.sam.newsapi.presentation.homescreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sam.newsapi.R
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.databinding.CategoryItemBinding
import com.sam.newsapi.presentation.homescreen.HomeScreenIntent.OnSectionItemClickIntent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class CategoryListAdapter @Inject constructor() :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    private val categoryList = ArrayList<Category>()
    private val categoryClickSubject = PublishSubject.create<OnSectionItemClickIntent>()

    val categoryClickEvent: Observable<OnSectionItemClickIntent> = categoryClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sectionItem = categoryList[position]
        holder.binding?.root?.setOnClickListener {
            categoryClickSubject.onNext(OnSectionItemClickIntent(sectionItem))
        }
        holder.binding?.sectionItem = sectionItem
    }

    override fun getItemCount() = categoryList.size

    fun setData(newCategoryList: List<Category>) {
        val diffResult = DiffUtil.calculateDiff(CategoryListDiffUtil(categoryList, newCategoryList))
        categoryList.clear()
        categoryList.addAll(newCategoryList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<CategoryItemBinding>(itemView)
    }
}