package com.sam.newsapi.presentation.homescreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sam.newsapi.R
import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.databinding.ArticleItemBinding
import com.sam.newsapi.presentation.homescreen.HomeScreenIntent.OnArticleClickIntent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ArticleListAdapter @Inject constructor() : RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {

    private val articleList = ArrayList<Article>()
    private val articleClickSubject = PublishSubject.create<OnArticleClickIntent>()

    val articleClickEvent: Observable<OnArticleClickIntent> = articleClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.article_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleItem = articleList[position]
        holder.binding?.root?.setOnClickListener {
            articleClickSubject.onNext(OnArticleClickIntent(articleItem))
        }
        holder.binding?.article = articleItem
    }

    fun setData(newArticleList: List<Article>) {
        val diffResult = DiffUtil.calculateDiff(ArticleListDiffUtil(articleList, newArticleList))
        articleList.clear()
        articleList.addAll(newArticleList)
        diffResult.dispatchUpdatesTo(this)

    }

    override fun getItemCount() = articleList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ArticleItemBinding>(itemView)
    }
}