package com.sam.newsapi.interactor.detailspage

import android.webkit.URLUtil
import com.sam.newsapi.interactor.detailspage.DetailsPageAction.*
import com.sam.newsapi.interactor.detailspage.DetailsPageResult.*
import com.sam.newsapi.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class DetailsPageActionProcessor @Inject constructor(
    private val schedulerProvider: BaseSchedulerProvider
) {

    internal var actionProcessor =
        ObservableTransformer<DetailsPageAction, DetailsPageResult> { actions ->
            actions.publish { shared ->
                Observable.merge<DetailsPageResult>(
                    shared.ofType(LoadDetailsPageAction::class.java).compose(
                        loadDetailsPageProcessor
                    ),
                    shared.ofType(OnNewsUrlClickAction::class.java).compose(newsUrlProcessor),
                    shared.ofType(DialogCancelAction::class.java).compose(dialogCancelProcessor)
                ).mergeWith(
                    shared.filter { action ->
                        action !is LoadDetailsPageAction &&
                                action !is OnNewsUrlClickAction &&
                                action !is DialogCancelAction
                    }.flatMap { w ->
                        Observable.error<DetailsPageResult>(IllegalArgumentException("Unknown Action type: $w"))
                    }
                )
            }
        }

    private val loadDetailsPageProcessor =
        ObservableTransformer<LoadDetailsPageAction, LoadDetailsPageResult> { actions ->
            actions.flatMap { action ->
                Observable.just(
                    LoadDetailsPageResult.Success(action.article)
                )
                    .cast(LoadDetailsPageResult::class.java)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(LoadDetailsPageResult.InFlight)
            }
        }

    private val newsUrlProcessor =
        ObservableTransformer<OnNewsUrlClickAction, OnNewsUrlClickResult> { actions ->
            actions.flatMap { action ->
                Observable.just(
                    if (URLUtil.isValidUrl(action.url)) {
                        OnNewsUrlClickResult.Success(action.url)
                    } else {
                        OnNewsUrlClickResult.Failure(Throwable("Url is invalid"))
                    }
                )
                    .cast(OnNewsUrlClickResult::class.java)
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