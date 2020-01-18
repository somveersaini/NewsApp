package com.sam.newsapi.di

import android.app.Application
import com.sam.newsapi.NewsApiApplication
import com.sam.newsapi.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        FragmentBuilder::class,
        ActivityBuilder::class,
        DataModule::class,
        SchedulersModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: NewsApiApplication)
}