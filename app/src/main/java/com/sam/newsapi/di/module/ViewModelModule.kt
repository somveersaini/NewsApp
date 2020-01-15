package com.sam.newsapi.di.module

import androidx.lifecycle.ViewModelProvider
import com.sam.newsapi.di.CustomViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory

}