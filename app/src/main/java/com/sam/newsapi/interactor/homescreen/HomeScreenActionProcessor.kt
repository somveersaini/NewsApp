package com.sam.newsapi.interactor.homescreen


import com.sam.newsapi.data.newsapi.NewsRepository
import com.sam.newsapi.data.newsapi.model.NewsModel
import com.sam.newsapi.util.schedulers.BaseSchedulerProvider
import com.sam.newsapi.interactor.homescreen.HomeScreenAction.*
import com.sam.newsapi.interactor.homescreen.HomeScreenResult.*
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
                    shared.ofType(LoadSectionNewsAction::class.java).compose(loadNewsListProcessor),
                    shared.ofType(RefreshAction::class.java).compose(refreshActionProcessor),
                    shared.ofType(OnNewsItemClickAction::class.java).compose(loadNewsActionProcessor),
                    shared.ofType(DialogCancelAction::class.java).compose(dailogCancelProcessor)
                ).mergeWith(
                    shared.filter { action ->
                        action !is LoadHomeScreenAction &&
                                action !is LoadSectionNewsAction &&
                                action !is RefreshAction &&
                                action !is OnNewsItemClickAction &&
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

    private val loadNewsListProcessor =
        ObservableTransformer<LoadSectionNewsAction, LoadNewsListResult> { actions ->
            actions.flatMap { action ->
                newsRepository.getNewsData(action.section).toObservable()
                    .map { LoadNewsListResult.Success(NewsModel(action.section, it.articles)) }
                    .cast(LoadNewsListResult::class.java)
                    .onErrorReturn(LoadNewsListResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadNewsListResult.InFlight)
            }
        }


    private val refreshActionProcessor =
        ObservableTransformer<RefreshAction, LoadNewsListResult> { actions ->
            actions.flatMap { action ->
                val section = action.sectionList.first { it.isChecked }
                newsRepository.getNewsData(section.name).toObservable()
                    .map { LoadNewsListResult.Success(NewsModel(section.name, it.articles)) }
                    .cast(LoadNewsListResult::class.java)
                    .onErrorReturn(LoadNewsListResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadNewsListResult.InFlight)
            }
        }

    private val loadNewsActionProcessor =
        ObservableTransformer<OnNewsItemClickAction, NewsItemClickResult> { actions ->
            actions.flatMap { action ->
                Observable.just(NewsItemClickResult(action.nyNewsListItem))
                    .cast(NewsItemClickResult::class.java)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }

    private val dailogCancelProcessor =
        ObservableTransformer<DialogCancelAction, DialogCancelResult> { actions ->
            actions.flatMap {
                Observable.just(DialogCancelResult)
                    .cast(DialogCancelResult::class.java)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
        }
}