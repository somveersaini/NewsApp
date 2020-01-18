package com.sam.newsapi.di.module

import com.sam.newsapi.presentation.detailspage.DetailsPageFragment
import com.sam.newsapi.presentation.homescreen.HomeScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun contributeHomeScreenFragment(): HomeScreenFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsPageFragment(): DetailsPageFragment
}