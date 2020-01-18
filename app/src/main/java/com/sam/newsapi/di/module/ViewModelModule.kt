package com.sam.newsapi.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sam.newsapi.di.CustomViewModelFactory
import com.sam.newsapi.di.ViewModelKey
import com.sam.newsapi.presentation.detailspage.DetailsPageViewModel
import com.sam.newsapi.presentation.homescreen.HomeScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeScreenViewModel::class)
    abstract fun bindHomeScreenViewModel(homeScreenViewModel: HomeScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsPageViewModel::class)
    abstract fun bindDetailsPageViewModel(detailsPageViewModel: DetailsPageViewModel): ViewModel

}