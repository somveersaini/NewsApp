package com.sam.newsapi.di.module

import com.sam.newsapi.presentation.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity
}