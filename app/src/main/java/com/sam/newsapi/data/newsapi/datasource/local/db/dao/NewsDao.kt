package com.sam.newsapi.data.newsapi.datasource.local.db.dao

import androidx.room.*
import com.sam.newsapi.data.newsapi.datasource.local.db.entity.NewsEntity
import io.reactivex.Single

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsEntity: NewsEntity)

    @Query("SELECT * from news WHERE news_id= :id LIMIT 1")
    fun getNewsEntityByNewsId(id: String): Single<NewsEntity>

    @Delete
    fun delete(newsEntity: NewsEntity)

    @Insert
    fun insertAll(data: List<NewsEntity>)

    @Query("SELECT * FROM news")
    fun getAll(): Single<List<NewsEntity>>

    @Query("DELETE FROM news")
    fun deleteAll()
}