package com.sam.newsapi.di.module

import com.sam.newsapi.util.schedulers.BaseSchedulerProvider
import com.sam.newsapi.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class SchedulersModule {

    @Provides
    fun providesSchedulers(): BaseSchedulerProvider = SchedulerProvider
}