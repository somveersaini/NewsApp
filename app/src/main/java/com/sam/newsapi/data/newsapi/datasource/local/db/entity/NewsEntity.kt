package com.sam.newsapi.data.newsapi.datasource.local.db.entity

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sam.newsapi.data.newsapi.model.Article
import java.lang.reflect.Type

@Entity(tableName = "news")
@TypeConverters(NewsListConverters::class)
data class NewsEntity(

    @PrimaryKey
    @ColumnInfo(name = "news_id")
    var news_id: String,

    @ColumnInfo(name = "newslist")
    var news_list: List<Article>
)

class NewsListConverters {

    @TypeConverter
    fun stringToNewslist(json: String?): List<Article> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Article?>?>() {}.type
        return gson.fromJson<List<Article>>(json, type)
    }

    @TypeConverter
    fun newslistToString(list: List<Article?>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Article?>?>() {}.type
        return gson.toJson(list, type)
    }
}