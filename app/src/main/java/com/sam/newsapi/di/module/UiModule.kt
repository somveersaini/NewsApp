package com.sam.newsapi.di.module

import android.content.Context
import com.sam.newsapi.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class UiModule {

    @Named("image_corner_radius")
    @Provides
    fun provideImageCornerRadius(context: Context): Int {
        return context.resources.getDimensionPixelSize(R.dimen.corner_radius)
    }
}