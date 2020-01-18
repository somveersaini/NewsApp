package com.sam.newsapi.presentation.homescreen

import androidx.lifecycle.ViewModel
import com.sam.newsapi.interactor.homescreen.HomeScreenAction
import com.sam.newsapi.interactor.homescreen.HomeScreenAction.*
import com.sam.newsapi.interactor.homescreen.HomeScreenActionProcessor
import com.sam.newsapi.interactor.homescreen.HomeScreenResult
import com.sam.newsapi.interactor.homescreen.HomeScreenResult.*
import com.sam.newsapi.presentation.base.MviViewModel
import com.sam.newsapi.presentation.homescreen.HomeScreenIntent.*
import com.sam.newsapi.presentation.homescreen.HomeScreenViewEffect.OpenDetailsPageEffect
import com.sam.newsapi.presentation.homescreen.HomeScreenViewEffect.RefreshEffect
import com.sam.newsapi.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(
    private val actionProcessorHolder: HomeScreenActionProcessor
) : ViewModel(), MviViewModel<HomeScreenIntent, HomeScreenViewState, HomeScreenViewEffect> {

    private val intentsSubject: PublishSubject<HomeScreenIntent> = PublishSubject.create()
    private val effect: PublishSubject<HomeScreenViewEffect> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    private val statesObservable by lazy { compose() }
    private val reducer by lazy { reducer() }


    override fun processIntents(intents: Observable<HomeScreenIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<HomeScreenViewState> = statesObservable

    override fun effects(): Observable<HomeScreenViewEffect> = effect

    private fun compose(): Observable<HomeScreenViewState> {
        return intentsSubject.compose<HomeScreenIntent>(intentFilter)
            .map<HomeScreenAction>(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .doOnNext(::provisionEffect)
            .scan(HomeScreenViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun provisionEffect(result: HomeScreenResult) {
        effectFromResult(result)?.let(effect::onNext)
    }

    private fun effectFromResult(result: HomeScreenResult): HomeScreenViewEffect? {
        return when (result) {
            is LoadHomeScreenResult.Success -> RefreshEffect(result.category)
            is ArticleClickResult -> OpenDetailsPageEffect(result.article)
            else -> null
        }
    }

    private fun reducer() =
        BiFunction { previousState: HomeScreenViewState, result: HomeScreenResult ->
            when (result) {
                is LoadHomeScreenResult.Failure -> previousState.copy(
                    isRefreshing = false,
                    error = result.error
                )
                is LoadCategoryArticlesResult.Failure -> previousState.copy(
                    isRefreshing = false,
                    error = result.error
                )
                is LoadHomeScreenResult.Success -> previousState.copy(
                    isRefreshing = false,
                    categoryList = result.category
                )
                is LoadCategoryArticlesResult.InFlight -> previousState.copy(
                    isRefreshing = true,
                    error = null
                )
                is LoadCategoryArticlesResult.Success -> previousState.updateCategory(result.data.key).copy(
                    isRefreshing = false,
                    articleList = result.data.articles
                )
                is DialogCancelResult -> previousState.copy(
                    error = null
                )
                else -> previousState
            }
        }

    private val intentFilter: ObservableTransformer<HomeScreenIntent, HomeScreenIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<HomeScreenIntent>(
                    shared.ofType(InitialIntent::class.java).take(1),
                    shared.notOfType(InitialIntent::class.java)
                )
            }
        }


    private fun actionFromIntent(intent: HomeScreenIntent): HomeScreenAction {
        return when (intent) {
            is InitialIntent -> LoadHomeScreenAction
            is OnArticleClickIntent -> ArticleClickAction(intent.article)
            is OnCategoryClickIntent -> CategoryClickAction(intent.category.name)
            ErrorDialogCancelIntent -> DialogCancelAction
            is RefreshIntent -> RefreshAction(intent.categoryList)
        }
    }

    override fun onCleared() {
        disposables.dispose()
    }

}