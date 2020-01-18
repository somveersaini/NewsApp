package com.sam.newsapi.interactor.homescreen


import com.sam.newsapi.data.newsapi.NewsRepository
import com.sam.newsapi.data.newsapi.model.NewsModel
import com.sam.newsapi.interactor.homescreen.HomeScreenAction.*
import com.sam.newsapi.interactor.homescreen.HomeScreenResult.*
import com.sam.newsapi.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class HomeScreenActionProcessor @Inject constructor(
    private val newsRepository: NewsRepository,
    private val schedulerProvider: BaseSchedulerProvider
) {

    internal var actionProcessor =
        ObservableTransformer<HomeScreenAction, HomeScreenResult> { actions ->
            actions.publish { shared ->
                Observable.mergeArray<HomeScreenResult>(
                    shared.ofType(LoadHomeScreenAction::class.java).compose(loadHomeScreenProcessor),
                    shared.ofType(CategoryClickAction::class.java).compose(loadCategoryArticleProcessor),
                    shared.ofType(RefreshAction::class.java).compose(refreshActionProcessor),
                    shared.ofType(ArticleClickAction::class.java).compose(articleClickActionProcessor),
                    shared.ofType(DialogCancelAction::class.java).compose(dialogCancelProcessor)
                ).mergeWith(
                    shared.filter { action ->
                        action !is LoadHomeScreenAction &&
                                action !is CategoryClickAction &&
                                action !is RefreshAction &&
                                action !is ArticleClickAction &&
                                action !is DialogCancelAction
                    }.flatMap { w ->
                        Observable.error<HomeScreenResult>(IllegalArgumentException("Unknown Action type: $w"))
                    }
                )
            }
        }

    private val loadHomeScreenProcessor =
        ObservableTransformer<LoadHomeScreenAction, LoadHomeScreenResult> { actions ->
            actions.flatMap {
                newsRepository.getCategoryList().toObservable()
                    .map(LoadHomeScreenResult::Success)
                    .cast(LoadHomeScreenResult::class.java)
                    .onErrorReturn(LoadHomeScreenResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }

    private val loadCategoryArticleProcessor =
        ObservableTransformer<CategoryClickAction, LoadCategoryArticlesResult> { actions ->
            actions.flatMap { action ->
                newsRepository.getNewsData(action.category).toObservable()
                    .map { LoadCategoryArticlesResult.Success(NewsModel(action.category, it.articles)) }
                    .cast(LoadCategoryArticlesResult::class.java)
                    .onErrorReturn(LoadCategoryArticlesResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadCategoryArticlesResult.InFlight)
            }
        }


    private val refreshActionProcessor =
        ObservableTransformer<RefreshAction, LoadCategoryArticlesResult> { actions ->
            actions.flatMap { action ->
                val category = action.categoryList.first { it.isChecked }
                newsRepository.getNewsData(category.name).toObservable()
                    .map { LoadCategoryArticlesResult.Success(NewsModel(category.name, it.articles)) }
                    .cast(LoadCategoryArticlesResult::class.java)
                    .onErrorReturn(LoadCategoryArticlesResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadCategoryArticlesResult.InFlight)
            }
        }

    private val articleClickActionProcessor =
        ObservableTransformer<ArticleClickAction, ArticleClickResult> { actions ->
            actions.flatMap { action ->
                Observable.just(ArticleClickResult(action.article))
                    .cast(ArticleClickResult::class.java)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }

    private val dialogCancelProcessor =
        ObservableTransformer<DialogCancelAction, DialogCancelResult> { actions ->
            actions.flatMap {
                Observable.just(DialogCancelResult)
                    .cast(DialogCancelResult::class.java)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }
}