package com.sam.newsapi.data.newsapi.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class NewsModel(
    @SerializedName("status") val key: String,
    @SerializedName("articles") val articles: List<Article>
)

@Keep
@Parcelize
data class Article(
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("urlToImage") val urlToImage: String,
    @SerializedName("source") val source: Source,
    @SerializedName("content") val content: String
): Parcelable


@Keep
@Parcelize
data class Source(
    @SerializedName("name") val name: String
): Parcelable