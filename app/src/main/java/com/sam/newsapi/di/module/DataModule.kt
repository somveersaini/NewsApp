package com.sam.newsapi.di.module

import android.content.Context
import com.sam.newsapi.data.newsapi.NewsRepository
import com.sam.newsapi.data.newsapi.NewsRepositoryImpl
import com.sam.newsapi.data.newsapi.datasource.local.db.NewsDatabase
import com.sam.newsapi.data.newsapi.datasource.remote.NewsApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository {
        return newsRepositoryImpl
    }

    @Provides
    @Singleton
    fun providerNewsApiService(): NewsApiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit
            .Builder()
            .client(httpClient)
            //.baseUrl(Utils.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun providerNewsDatabase(context: Context): NewsDatabase {
        return NewsDatabase(context)
    }
}