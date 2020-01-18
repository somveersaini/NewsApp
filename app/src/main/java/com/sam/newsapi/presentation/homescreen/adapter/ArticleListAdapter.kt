package com.sam.newsapi.presentation.homescreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sam.newsapi.R
import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.databinding.ArticleItemBinding
import com.sam.newsapi.presentation.homescreen.HomeScreenIntent.OnNewsItemClickIntent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class ArticleListAdapter @Inject constructor(
    @Named("image_corner_radius") val imageCornerRadius: Int
) : RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {

    private val articleList = ArrayList<Article>()
    private val articleClickSubject = PublishSubject.create<OnNewsItemClickIntent>()

    val articleClickEvent: Observable<OnNewsItemClickIntent> = articleClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.article_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleItem = articleList[position]
        holder.binding?.root?.setOnClickListener {
            articleClickSubject.onNext(OnNewsItemClickIntent(articleItem))
        }
        holder.binding?.article = articleItem
        holder.binding?.let { binding ->
            Glide.with(binding.root)
                .load(articleItem.urlToImage)
                .fitCenter()
                .transform(RoundedCorners(imageCornerRadius))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.newsImage)
        }
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